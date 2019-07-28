package td4

import chisel3._

class Td4() extends Module {
  val io = IO(new Bundle{
    val core_io = new CoreIO()
  })

  val pc = RegInit(0.U(4.W))
  val out = RegInit(0.U(4.W))

  val decoder = Module(new Decoder())
  val registers = Module(new RegisterFile())
  val alu = Module(new Alu())
  val imm_gen = Module(new ImmGen())
  val pc_incrementer = Module(new Adder())

  val next_pc = Wire(UInt())
  pc_incrementer.io.in_a := pc
  pc_incrementer.io.in_b := 1.U
  next_pc := pc_incrementer.io.out

  io.core_io.read_addr := pc
  decoder.io.insn := io.core_io.read_data
  imm_gen.io.insn := io.core_io.read_data

  registers.io.read_reg := decoder.io.read_reg
  registers.io.write_reg := decoder.io.write_reg
  registers.io.write_en := decoder.io.write_en

  when (decoder.io.in_en) {
    alu.io.in_a := io.core_io.in
  } .otherwise {
    alu.io.in_a := registers.io.read_data
  }
  when (decoder.io.alu_en) {
    alu.io.in_b := imm_gen.io.imm
  } .otherwise {
    alu.io.in_b := 0.U
  }

  registers.io.write_data := alu.io.out_s

  io.core_io.out := out
  when (decoder.io.out_en) {
    out := alu.io.out_s
  }

  when (decoder.io.jmp_en) {
    pc := imm_gen.io.imm
  } .elsewhen (decoder.io.jnc_en && alu.io.out_c) {
    pc := imm_gen.io.imm
  } .otherwise {
    pc := next_pc
  }
}
