package org.biojava.nbio.structure.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.junit.Test;


public class TestMMCIFSeqResParsing {

	
	@Test
	public void testParseProteinPdbxPolySeqScheme() throws IOException, StructureException {
		
		// In this entry _pdbx_struct_assembly_gen contains multiline quoting (quoting with "\n;" ) in loop field
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		StructureIO.setAtomCache(cache); 
				
		FileParsingParameters params = cache.getFileParsingParams();
		params.setParseBioAssembly(true);
		params.setAlignSeqRes(true);
		
		StructureIO.setAtomCache(cache);

		Structure sCif = StructureIO.getStructure("3M9V");
		
		//Test a protein with mismatching more SeqRes defined than AminoAcids in Structure.
		Chain chainA = sCif.getChainByPDB("A");
		List<Group> atomGroups = chainA.getAtomGroups(GroupType.AMINOACID);
		List<Group> seqRes = chainA.getSeqResGroups();
		
		assertNotNull(seqRes);
		assertNotEquals(atomGroups.size(), seqRes.size());
		
		
		assertEquals(439, seqRes.size());
	}
	
	@Test
	public void testParseNucleotidePdbxPolySeqScheme() throws IOException, StructureException {
		// In this entry _pdbx_struct_assembly_gen contains multiline quoting (quoting with "\n;" ) in loop field
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		StructureIO.setAtomCache(cache); 
				
		FileParsingParameters params = cache.getFileParsingParams();
		params.setParseBioAssembly(true);
		params.setAlignSeqRes(true);
		
		StructureIO.setAtomCache(cache);

		Structure sCif = StructureIO.getStructure("1OFX");
		
		// Tests for matching SeqRes groups vs Nucleotide groups.
		
		// Check a DNA only chain.
		Chain chainA = sCif.getChainByPDB("A");
		List<Group> atomGroups = chainA.getAtomGroups(GroupType.NUCLEOTIDE);
		List<Group> seqRes = chainA.getSeqResGroups();
		
		assertNotNull(seqRes);
		assertEquals(atomGroups.size(), seqRes.size());
		
		// Check a DNA/RNA chain.
		Chain chainB = sCif.getChainByPDB("B");
		List<Group> atomGroups2 = chainB.getAtomGroups(GroupType.NUCLEOTIDE);
		List<Group> seqRes2 = chainB.getSeqResGroups();
		
		assertNotNull(seqRes2);
		assertEquals(atomGroups2.size(), seqRes2.size());
	}
	
	@Test
	public void testAtomLigands() throws IOException, StructureException {
		// In this entry _pdbx_struct_assembly_gen contains multiline quoting (quoting with "\n;" ) in loop field
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		StructureIO.setAtomCache(cache); 
				
		FileParsingParameters params = cache.getFileParsingParams();
		params.setParseBioAssembly(true);
		params.setAlignSeqRes(true);
		
		StructureIO.setAtomCache(cache);

		Structure sCif = StructureIO.getStructure("4HHB");
		
		Chain chainA = sCif.getChainByPDB("A");
		
		List<Group> seqres = chainA.getSeqResGroups();
		
		assertEquals(141, seqres.size());
		
		for (Group g1 : chainA.getAtomGroups(GroupType.AMINOACID)) {
			// Test that the chainA contains all the AtomGroups we expect it to contain.
			System.out.println(g1.getPDBName() + g1.getResidueNumber().toString());
			assertTrue(seqres.contains(g1));
		}
		
		assertEquals(1, chainA.getAtomLigands().size());
	}
	
	
	/*
	 * This bizarre mmCIF has pdbx poly seq scheme entries for chain L
	 * That are missing sequential pdb_seq_num (all the same) and are using insertion
	 * codes to differentiate between the numbers.  It is probably just incorrectly 
	 * written, but is causing this unit test to fail. 
	 * (It may be an exception to the rules).
	 */
	@Test
	public void test1A4W() throws IOException, StructureException {
		// In this entry _pdbx_struct_assembly_gen contains multiline quoting (quoting with "\n;" ) in loop field
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		StructureIO.setAtomCache(cache); 
				
		FileParsingParameters params = cache.getFileParsingParams();
		params.setParseBioAssembly(true);
		params.setAlignSeqRes(true);
		
		StructureIO.setAtomCache(cache);

		Structure sCif = StructureIO.getStructure("1A4W");
		
		Chain chainL = sCif.getChainByPDB("L");
		assertEquals(36, chainL.getSeqResGroups().size());
	}
}
