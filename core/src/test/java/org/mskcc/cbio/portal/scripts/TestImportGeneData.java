/*
 * Copyright (c) 2017 The Hyve B.V.
 * This code is licensed under the GNU Affero General Public License (AGPL),
 * version 3, or (at your option) any later version.
 */

/*
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * @author Sander Tan
*/

package org.mskcc.cbio.portal.scripts;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.mskcc.cbio.portal.dao.DaoGeneOptimized;
import org.mskcc.cbio.portal.model.CanonicalGene;
import org.mskcc.cbio.portal.util.ProgressMonitor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * JUnit tests for ImportGeneData class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TestImportGeneData {

    @Test
    public void testImportGeneData() throws Exception {
        DaoGeneOptimized daoGene = DaoGeneOptimized.getInstance();
        ProgressMonitor.setConsoleMode(false);
	
        // TBD: change this to use getResourceAsStream()
        File file = new File("src/test/resources/supp-genes.txt");

        ImportGeneData.importSuppGeneData(file);
        
        file = new File("src/test/resources/genes_test.txt");
        ImportGeneData.importData(file);

        CanonicalGene gene = daoGene.getGene(10);
        assertEquals("NAT2", gene.getHugoGeneSymbolAllCaps());
        gene = daoGene.getGene(15);
        assertEquals("AANAT", gene.getHugoGeneSymbolAllCaps());

        gene = daoGene.getGene("ABCA3");
        assertEquals(21, gene.getEntrezGeneId());
    }
}