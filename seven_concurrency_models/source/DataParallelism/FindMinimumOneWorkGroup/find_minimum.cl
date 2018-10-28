__kernel void find_minimum(__global const float* values,
                           __global float* result,
                           __local float* scratch) {
  int i = get_global_id(0);
  int n = get_global_size(0);
  scratch[i] = values[i]; //<label id="code.copytolocal"/>
  barrier(CLK_LOCAL_MEM_FENCE); //<label id="code.barrier0"/>
  for (int j = n / 2; j > 0; j /= 2) { //<label id="code.reduce0"/>
    if (i < j)
      scratch[i] = min(scratch[i], scratch[i + j]);
    barrier(CLK_LOCAL_MEM_FENCE); //<label id="code.barrier1"/>
  } //<label id="code.reduce1"/>
  if (i == 0)
    *result = scratch[0]; //<label id="code.copyresult"/>
}