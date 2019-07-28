package td4

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class MemoryUnitTest(m: InstMemory) extends PeekPokeTester(m) {
  for (i <- 0 until 15) {
    poke(m.io.read_addr, i)
    expect(m.io.read_data, i)
  }
}

class MemoryTester extends ChiselFlatSpec {
  "InstMemory" should s"read data correctly" in {
    Driver(() => new InstMemory("src/test/resources/raw/inc.txt"), "treadle") {
      m => new MemoryUnitTest(m)
    } should be (true)
  }
}