/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
assertNotNull(netcdf, "no input provided");
var expectedLonBnds = [
  [ 0, 2 ], [ 2, 4 ], [ 4, 6 ], [ 6, 8 ], [ 8, 10 ], [ 10, 12 ], [ 12, 14 ], [ 14, 16 ], [ 16, 18 ], [ 18, 20 ],
  [ 20, 22 ], [ 22, 24 ], [ 24, 26 ], [ 26, 28 ], [ 28, 30 ], [ 30, 32 ], [ 32, 34 ], [ 34, 36 ], [ 36, 38 ],
  [ 38, 40 ], [ 40, 42 ], [ 42, 44 ], [ 44, 46 ], [ 46, 48 ], [ 48, 50 ], [ 50, 52 ], [ 52, 54 ], [ 54, 56 ],
  [ 56, 58 ], [ 58, 60 ], [ 60, 62 ], [ 62, 64 ], [ 64, 66 ], [ 66, 68 ], [ 68, 70 ], [ 70, 72 ], [ 72, 74 ],
  [ 74, 76 ], [ 76, 78 ], [ 78, 80 ], [ 80, 82 ], [ 82, 84 ], [ 84, 86 ], [ 86, 88 ], [ 88, 90 ], [ 90, 92 ],
  [ 92, 94 ], [ 94, 96 ], [ 96, 98 ], [ 98, 100 ], [ 100, 102 ], [ 102, 104 ], [ 104, 106 ], [ 106, 108 ],
  [ 108, 110 ], [ 110, 112 ], [ 112, 114 ], [ 114, 116 ], [ 116, 118 ], [ 118, 120 ], [ 120, 122 ], [ 122, 124 ],
  [ 124, 126 ], [ 126, 128 ], [ 128, 130 ], [ 130, 132 ], [ 132, 134 ], [ 134, 136 ], [ 136, 138 ], [ 138, 140 ],
  [ 140, 142 ], [ 142, 144 ], [ 144, 146 ], [ 146, 148 ], [ 148, 150 ], [ 150, 152 ], [ 152, 154 ], [ 154, 156 ],
  [ 156, 158 ], [ 158, 160 ], [ 160, 162 ], [ 162, 164 ], [ 164, 166 ], [ 166, 168 ], [ 168, 170 ], [ 170, 172 ],
  [ 172, 174 ], [ 174, 176 ], [ 176, 178 ], [ 178, 180 ], [ 180, 182 ], [ 182, 184 ], [ 184, 186 ], [ 186, 188 ],
  [ 188, 190 ], [ 190, 192 ], [ 192, 194 ], [ 194, 196 ], [ 196, 198 ], [ 198, 200 ], [ 200, 202 ], [ 202, 204 ],
  [ 204, 206 ], [ 206, 208 ], [ 208, 210 ], [ 210, 212 ], [ 212, 214 ], [ 214, 216 ], [ 216, 218 ], [ 218, 220 ],
  [ 220, 222 ], [ 222, 224 ], [ 224, 226 ], [ 226, 228 ], [ 228, 230 ], [ 230, 232 ], [ 232, 234 ], [ 234, 236 ],
  [ 236, 238 ], [ 238, 240 ], [ 240, 242 ], [ 242, 244 ], [ 244, 246 ], [ 246, 248 ], [ 248, 250 ], [ 250, 252 ],
  [ 252, 254 ], [ 254, 256 ], [ 256, 258 ], [ 258, 260 ], [ 260, 262 ], [ 262, 264 ], [ 264, 266 ], [ 266, 268 ],
  [ 268, 270 ], [ 270, 272 ], [ 272, 274 ], [ 274, 276 ], [ 276, 278 ], [ 278, 280 ], [ 280, 282 ], [ 282, 284 ],
  [ 284, 286 ], [ 286, 288 ], [ 288, 290 ], [ 290, 292 ], [ 292, 294 ], [ 294, 296 ], [ 296, 298 ], [ 298, 300 ],
  [ 300, 302 ], [ 302, 304 ], [ 304, 306 ], [ 306, 308 ], [ 308, 310 ], [ 310, 312 ], [ 312, 314 ], [ 314, 316 ],
  [ 316, 318 ], [ 318, 320 ], [ 320, 322 ], [ 322, 324 ], [ 324, 326 ], [ 326, 328 ], [ 328, 330 ], [ 330, 332 ],
  [ 332, 334 ], [ 334, 336 ], [ 336, 338 ], [ 338, 340 ], [ 340, 342 ], [ 342, 344 ], [ 344, 346 ], [ 346, 348 ],
  [ 348, 350 ], [ 350, 352 ], [ 352, 354 ], [ 354, 356 ], [ 356, 358 ], [ 358, 360 ]
];
assertNotNull(netcdf.lonBnds, "/lon_bnds");
assertTrue(arrayEquals(netcdf.lonBnds.value, expectedLonBnds), "/lon_bnds value");
assertEquals(netcdf.time.units, "days since 2001-1-1", "/time/@units");
assertEquals(netcdf.tos.axis, null, "/tos/@axis (should not exist)");
