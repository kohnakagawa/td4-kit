package td4

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class ImmGenUnitTest(c: ImmGen) extends PeekPokeTester(c) {
  private val imm_gen = c
  poke(imm_gen.io.insn, "b01101010".U(8.W))
  expect(imm_gen.io.imm, 10)
}

class ImmGenTester extends ChiselFlatSpec {
  "ImmGen" should s"generate immediate value correctly" in {
    Driver(() => new ImmGen()) {
      c => new ImmGenUnitTest(c)
    } should be (true)
  }
}

