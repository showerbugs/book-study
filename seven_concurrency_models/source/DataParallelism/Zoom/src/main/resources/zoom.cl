__kernel void zoom(__global float* vertices) {

  unsigned int id = get_global_id(0);
  vertices[id] *= 1.01;
}
