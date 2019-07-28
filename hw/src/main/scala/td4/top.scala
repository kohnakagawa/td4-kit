package td4

import chisel3._

class Top extends Module {
  val io = IO(new Bundle{
    val in = Input(UInt(4.W))

    val out = Output(UInt(4.W))
    val success = Output(Bool())
  })

  io.success := DontCare

  val cpu = Module(new Td4())
  val mem = Module(new InstMemory("test.bin"))

  mem.io.read_addr := cpu.io.core_io.read_addr
  cpu.io.core_io.read_data := mem.io.read_data

  cpu.io.core_io.in := io.in
  io.out := cpu.io.core_io.out
}
