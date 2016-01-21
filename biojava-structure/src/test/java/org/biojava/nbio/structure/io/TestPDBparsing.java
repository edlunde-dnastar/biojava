package org.biojava.nbio.structure.io;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.junit.Test;

/**
 * The PDB Parser in 4.1.x has a couple of flaws:
 * 1. it fails to correctly alignSeqRes to include HETATM groups
 * in the sequence.  Instead these partition as ligands. 
 * 
 * examples: 1DAR, 2IZQ, 1AIS, 3T5N, and 3AJK.
 * 
 * 2. CONECT lines are not checked for length
 * 3. CONECT lines toPDB() need to be 80 character.
 * 
 * Unit test to fix PDB parser bugs.
 * <p>(c) 2016 DNASTAR, Inc.</p>
 * @since Jan 21, 2016
 * @author larsonm
 */
public class TestPDBparsing {
	@Test
	public void testParse2IZQ() throws IOException, StructureException {
        AtomCache cache = new AtomCache();
        
        FileParsingParameters params = new FileParsingParameters();
        params.setParseSecStruc(true);
        params.setAlignSeqRes(true);
        params.setParseCAOnly(false);
        cache.setFileParsingParams(params);
        
        
        StructureIO.setAtomCache(cache);
        cache.setUseMmCif(false);
        
        Structure sPdb = StructureIO.getStructure("2IZQ");

        assertNotNull(sPdb);
        
        // Examine the SEQRES for the chains.
        // All SEQRES groups should be instances with atoms.
        
        for (Group g : sPdb.getChain(0).getSeqResGroups()) {
        	// Assert.assertTrue("Group failed : " + g.getPDBName(), g.getAtoms().size() > 0);
        	System.out.println(g.getPDBName() + " " + g.getAtoms().size());
        }
        // OK, so the seqres has empty residues, is that bad?
        List<Group> ligands = sPdb.getChain(0).getAtomLigands();
        for (Group g : ligands) {
        	// Assert.assertTrue("Group failed : " + g.getPDBName(), g.getAtoms().size() > 0);
        	System.out.println(g.getPDBName() + " " + g.getAtoms().size());
        }
	}
}
