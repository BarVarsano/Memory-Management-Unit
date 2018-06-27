package com.hit.memoryunits;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.Random;
import junit.framework.Assert;
/*
 * Bar Varsano 307929497
 * Amit Levy 305650517
 */
@SuppressWarnings("deprecation")
public class MemoryManagementUnitTest {

	private Long[] testPages1 = new Long[3];
	private Long[] testPages2 = new Long[1];

	@Before
	public void setUp() {
		for (int i = 0; i < 3; i++) {
			testPages1[i] = (long) (i + 1);
		}
		testPages2[0] = 4L;
	}

	@Test
	public void test() throws ClassNotFoundException, IOException {
		System.out.println("\n ########################################## \n" + "       Memory Management Unit Test    "
				+ "\n ########################################## \n");

		IAlgoCache<Long, Long> algo = new LRUAlgoCacheImpl<Long, Long>(3);
		RAM ramCheck = new RAM(3);
		MemoryManagementUnit mmu = new MemoryManagementUnit(3, algo);

		Page<byte[]>[] pagesRet = mmu.getPages(testPages1);
		System.out.println("The pages returned from the original RAM:");
		System.out.println(Arrays.toString(testPages1) + "\n" + "\n");

		System.out.println("The RAM used for testing");
		System.out.println("in initial mode:");
		System.out.println(ramCheck.toString() + "\n");

		/* Check for data loss */
		byte[] page1Content = pagesRet[0].getContent();
		Long page1Key = pagesRet[0].getPageId();

		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesCheck1 = (Page<byte[]>[]) new Page[3];
		for (int i = 0; i < 3; i++) {
			pagesCheck1[i] = new Page<byte[]>((long) i + 1,
					HardDisk.getInstance().pageFault((long) (i + 1)).getContent());
			assertEquals(pagesRet[i].toString(), pagesCheck1[i].toString());
			ramCheck.addPage(pagesCheck1[i]);
		}

		System.out.println("after the insert values:");
		System.out.println(ramCheck.toString() + "\n" + "\n");
		assertEquals(ramCheck.toString(), mmu.getRam().toString());

		System.out.println("Original RAM:");
		System.out.println("getPage:" + testPages2[0].toString() + "\n");
		pagesRet = mmu.getPages(testPages2);

		System.out.println("The RAM used for testing:");
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pagesCheck2 = (Page<byte[]>[]) new Page[1];
		pagesCheck2[0] = new Page<byte[]>(4L, HardDisk.getInstance().pageFault(4L).getContent());
		assertEquals(pagesRet[0].toString(), pagesCheck2[0].toString());

		System.out.println("Delete page:" + pagesCheck1[0].toString());
		ramCheck.removePage(pagesCheck1[0]);

		System.out.println("Add page:" + testPages2[0].toString() + "/n");
		ramCheck.addPage(pagesCheck2[0]);

		System.out.println("\n" + "The RAM used for testing end of test:");
		System.out.println(ramCheck.toString() + "\n");
		System.out.println("The original RAM end of test:");
		System.out.println(mmu.getRam().toString() + "\n" + "\n");
		assertEquals(ramCheck.toString(), mmu.getRam().toString());

		assertEquals(ramCheck.toString(), mmu.getRam().toString());

		/* Check for data loss */
		assertEquals(page1Content.toString(), HardDisk.getInstance().pageFault(page1Key).getContent().toString());

	}
}
