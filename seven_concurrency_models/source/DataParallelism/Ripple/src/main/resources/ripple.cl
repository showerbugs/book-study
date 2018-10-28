#define AMPLITUDE 0.1
#define FREQUENCY 10.0
#define SPEED 0.5
#define WAVE_PACKET 50.0
#define DECAY_RATE 2.0
__kernel void ripple(__global float* vertices,
                     __global float* centers,
                     __global long* times,
                     int num_centers,
                     long now) {
  unsigned int id = get_global_id(0);
  unsigned int offset = id * 3;
  float x = vertices[offset]; //<label id="code.xcoord"/>
  float y = vertices[offset + 1]; //<label id="code.ycoord"/>
  float z = 0.0;

  for (int i = 0; i < num_centers; ++i) { //<label id="code.loopstart"/>
    if (times[i] != 0) {
      float dx = x - centers[i * 2]; //<label id="code.xdelta"/>
      float dy = y - centers[i * 2 + 1]; //<label id="code.ydelta"/>
      float d = sqrt(dx * dx + dy * dy); //<label id="code.calcd"/>
      float elapsed = (now - times[i]) / 1000.0;
      float r = elapsed * SPEED; //<label id="code.calcr"/>
      float delta = r - d; //<label id="code.delta"/>
      z += AMPLITUDE *
        exp(-DECAY_RATE * r * r) * 
        exp(-WAVE_PACKET * delta * delta) *
        cos(FREQUENCY * M_PI_F * delta);
    }
  } //<label id="code.loopend"/>
  vertices[offset + 2] = z; //<label id="code.writez"/>
}
