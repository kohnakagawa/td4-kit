package td4

import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class AdderUnitTest(c: Adder) extends PeekPokeTester(c) {
  private val adder = c
  poke(adder.io.in_a, 1)
  poke(adder.io.in_b, 2)
  expect(adder.io.out, 3)
}

class AdderTester extends ChiselFlatSpec {
  "Adder" should s"add two integaer values correctly" in {
    Driver(() => new Adder()) {
      c => new AdderUnitTest(c)
    } should be (true)
  }
}

