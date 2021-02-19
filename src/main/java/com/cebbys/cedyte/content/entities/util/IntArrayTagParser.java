package com.cebbys.cedyte.content.entities.util;

import com.cebbys.cedyte.Cedyte;
import com.cebbys.celib.loggers.CelibLogger;

import net.minecraft.util.math.BlockPos;

public class IntArrayTagParser {

	
	public static class PosArray {
		
		
		public static int length(int[] a) {
			return (a.length / 3);
		}

		
		public static int[] erase(int[] a, int index) {
			if (length(a) > 1 && index >= 0 && index < length(a)) {
				int[] n = new int[a.length - 3];
				int ci = 0;
				for (int i = 0; i < a.length; i++) {
					if (i / 3 != index) {
						n[ci++] = a[i];
					}
				}
				return n;
			} else {
				return new int[0];
			}
		}

		
		public static int[] insert(int[] base, int[] ins, int pos) {
			if ((ins.length > 2 && ins.length % 3 == 0) && (pos >= 0 && pos <= base.length)) {
				int[] n = new int[base.length + 3];
				int ci = 0;
				for (int i = 0; i < n.length; i++) {
					if (i / 3 == pos) {
						n[i] = ins[i % 3];
					} else {
						n[i] = base[ci++];
					}
				}
			} else {
				CelibLogger.error(Cedyte.MOD_ID, "[ PosArray ] " + "Insertion failed");
			}
			return base;
		}
		
		
		public static BlockPos asBlockPos( int[] a ) {
			BlockPos pos = null;
			if( a.length == 3 ) {
				pos = new BlockPos( a[ 0 ], a[ 1 ], a[ 2 ] );
			}
			return pos;
		}
		
		
		public static BlockPos[] toBlockPosArray( int[] a ) {
			BlockPos[] arr = new BlockPos[ 0 ];
			if( a.length % 3 == 0 ) {
				arr = new BlockPos[ length( a ) ];
				for( int i = 0; i < arr.length; i++ ) {
					arr[ i ] = new BlockPos( a[ i * 3 ], a[ i * 3 + 1 ], a[ i * 3 + 2 ] );
				}
			}
			return arr;
		}
		
		
		public static int[] toIntArray( BlockPos[] a ) {
			int[] arr = new int[ a.length * 3 ];
			for( int i = 0; i < a.length; i++ ) {
				arr[ i * 3 + 0 ] = a[ i ].getX();
				arr[ i * 3 + 1 ] = a[ i ].getY();
				arr[ i * 3 + 2 ] = a[ i ].getZ();
			}
			return arr;
		}
	}
}
