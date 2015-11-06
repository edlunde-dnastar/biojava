package org.biojava.nbio.structure.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

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
}
