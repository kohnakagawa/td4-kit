package td4

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class AluUnitTester(c: Alu) extends PeekPokeTester(c) {
  private val alu = c
  poke(alu.io.in_a, 8)
  poke(alu.io.in_b, 8)
  expect(alu.io.out_s, 0)
  expect(alu.io.out_c, 1)

  poke(alu.io.in_a, 1)
  poke(alu.io.in_b, 1)
  expect(alu.io.out_s, 2)
  expect(alu.io.out_c, 0)
}

class AluTester extends ChiselFlatSpec {
  "Alu" should "add two values correctly" in {
    iotesters.Driver.execute(Array(), () => new Alu) {
      c => new AluUnitTester(c)
    } should be (true)
  }
}
