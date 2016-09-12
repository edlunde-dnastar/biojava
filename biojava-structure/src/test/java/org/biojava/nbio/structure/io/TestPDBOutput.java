package org.biojava.nbio.structure.io;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.junit.Test;

public class TestPDBOutput {
	/**
	 * All groups are expected to be empty.
	 * 
	 * @throws StructureException
	 * @throws IOException
	 */
	@Test
	public void testHeaderOnly() throws StructureException, IOException {
		// Get either PDB or mmCIF with a headerOnly = true.
		
		// Test 1: with PDB
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(false);
		
		FileParsingParameters params = new FileParsingParameters();
		// params.setHeaderOnly(true);
		// params.setAlignSeqRes(true);  // Now this is default.
		cache.setFileParsingParams(params);
		
		StructureIO.setAtomCache(cache); 
		
		Structure sPDB = StructureIO.getStructure("4HHB");
		String output = sPDB.toPDB();
		System.out.println(output);
	}
}
