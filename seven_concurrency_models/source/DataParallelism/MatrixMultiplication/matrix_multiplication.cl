// START:kernel
__kernel void matrix_multiplication(uint widthA,
                                    __global const float* inputA,
                                    __global const float* inputB,
                                    __global float* output) {

  int i = get_global_id(0); //<label id="code.globalid0"/>
  int j = get_global_id(1); //<label id="code.globalid1"/>

// END:kernel
  // Note that:
  // outputWidth == widthB
  // outputHeight == heightA
  // widthA == heightB
// START:kernel
  int outputWidth = get_global_size(0); //<label id="code.globalsize0"/>
  int outputHeight = get_global_size(1); //<label id="code.globalsize1"/>
  int widthB = outputWidth;

  float total = 0.0;
  for (int k = 0; k < widthA; ++k) { //<label id="code.innerloop"/>
    total += inputA[j * widthA + k] * inputB[k * widthB + i];
  }
  output[j * outputWidth + i] = total;
}
// END:kernel
