package td4

import chisel3._

class RegisterFile extends Module {
  val io = IO(new Bundle {
    val read_reg = Input(UInt(2.W))  // 読み込み対象のレジスタ 0:A, 1:B, 2:Zero Zeroは代入を許さないことにより、0を入れておく
    val write_reg = Input(UInt(2.W)) // 書き込み対象のレジスタ 0:A, 1:B, 2:Zero
    val write_en = Input(Bool()) // レジスタへの書き込みを行うかどうか
    val write_data = Input(UInt(4.W)) // 書き込むデータ

    val read_data = Output(UInt(4.W)) // 読み込んだデータ
  })

  val regs = RegInit(VecInit(Seq.fill(3)(0.U(4.W)))) // 0で初期化した上で3つのレジスタを定義

  when (io.write_en) {
    regs(io.write_reg) := io.write_data
  }

  io.read_data := regs(io.read_reg)
}
