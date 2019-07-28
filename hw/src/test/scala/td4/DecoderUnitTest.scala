package td4

import chisel3._
import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, PeekPokeTester}

class DecoderUnitTester(c: Decoder) extends PeekPokeTester(c) {
  private val decoder = c

  val tests = List(
    ( "b00110101".U, true.B, 2.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
    ( "b01110101".U, true.B, 2.U, 1.U, true.B, false.B, false.B, false.B, false.B, false.B),
    ( "b00010000".U, true.B, 1.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
    ( "b01000000".U, true.B, 1.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
    ( "b00000101".U, true.B, 2.U, 0.U, true.B, false.B, false.B, true.B , false.B, false.B),
    ( "b01010110".U, true.B, 2.U, 1.U, true.B, false.B, false.B, true.B , false.B, false.B),
    ( "b00100000".U, true.B, 2.U, 0.U, true.B, true.B , false.B, false.B, false.B, false.B),
    ( "b01100000".U, true.B, 2.U, 1.U, true.B, true.B , false.B, false.B, false.B, false.B),
    ( "b10111101".U, true.B, 2.U, 0.U, false.B, false.B, true.B, true.B , false.B, false.B),
    ( "b10010000".U, true.B, 1.U, 0.U, false.B, false.B, true.B, true.B , false.B, false.B),
    ( "b11111011".U, true.B, 0.U, 0.U, false.B, false.B, false.B, false.B, true.B, false.B),
    ( "b11101101".U, true.B, 0.U, 0.U, false.B, false.B, false.B, false.B, false.B, true.B),
    ( "b11010000".U, false.B, 0.U, 0.U, false.B, false.B, false.B, false.B, false.B, false.B)
  )

  for (t <- tests) {
    poke(decoder.io.insn, t._1)
    step(1)
    expect(decoder.io.valid_inst, t._2)
    expect(decoder.io.read_reg, t._3)
    expect(decoder.io.write_reg, t._4)
    expect(decoder.io.write_en, t._5)
    expect(decoder.io.in_en, t._6)
    expect(decoder.io.out_en, t._7)
    expect(decoder.io.alu_en, t._8)
    expect(decoder.io.jmp_en, t._9)
    expect(decoder.io.jnc_en, t._10)
  }
}

class DecoderTester extends ChiselFlatSpec {
  "Decoder" should "decode instruction correctly" in {
    iotesters.Driver.execute(Array(), () => new Decoder) {
      c => new DecoderUnitTester(c)
    } should be (true)
  }
}
