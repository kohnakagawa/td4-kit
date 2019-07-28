package td4

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class RegisterFileUnitTest(c: RegisterFile) extends PeekPokeTester(c) {
  private val reg_files = c

  poke(reg_files.io.read_reg, 0)
  poke(reg_files.io.write_reg, 1)
  poke(reg_files.io.write_en, true)
  poke(reg_files.io.write_data, 2)
  step(1)
  expect(reg_files.io.read_data, 0)

  poke(reg_files.io.read_reg, 1)
  poke(reg_files.io.write_reg, 1)
  poke(reg_files.io.write_en, false)
  poke(reg_files.io.write_data, 1)
  step(1)
  expect(reg_files.io.read_data, 2)
}

class RegisterFileTester extends ChiselFlatSpec {
  "RegisterFile" should s"holds values" in {
    Driver(() => new RegisterFile) {
      c => new RegisterFileUnitTest(c)
    } should be (true)
  }
}
