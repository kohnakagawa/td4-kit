#ifndef INSTS_HPP
#define INSTS_HPP

const uint8_t uint4_t_max = 0b1111;

void add_a_im(const uint8_t im) {
  const auto sum = a_.lo + im;
  a_.lo = sum;
  cflag_ = (sum > uint4_t_max);
}

void mov_a_b() {
  a_.lo = b_.lo;
  cflag_ = 0;
}

void in_a() {
  in_.lo = a_.lo;
  cflag_ = 0;
}

void mov_a_im(const uint8_t im) {
  a_.lo = im;
  cflag_ = 0;
}

void mov_b_a(const uint8_t im) {
  b_.lo = a_.lo;
  cflag_ = 0;
}

void add_b_im(const uint8_t im) {
  const auto sum = b_.lo + im;
  b_.lo = sum;
  cflag_ = (sum > uint4_t_max);
}

void in_b(const uint8_t im) {
  in_.lo = im;
  cflag_ = 0;
}

void mov_b_im(const uint8_t im) {
  b_.lo = im;
  cflag_ = 0;
}

[[ noreturn ]] void invalid() {
  throw std::runtime_error("invalid instruction");
}

void out_b() {
  out_.lo = b_.lo;
  cflag_ = 0;
}

void out_im(const uint8_t im) {
  out_.lo = im;
  cflag_ = 0;
}

void jnc(const uint8_t im) {
  if (!cflag_) {
    pc_.lo = im;
  }
  cflag_ = 0;
}

void jmp(const uint8_t im) {
  pc_.lo = im;
  cflag_ = 0;
}

void execute(const Inst& inst) {
  const auto opcode = inst.code.hi;
  const auto im = inst.code.lo;

  switch (opcode) {
  case 0b0000:
    add_a_im(im);
    break;
  case 0b0001:
    mov_a_b();
    break;
  case 0b0010:
    in_a();
    break;
  case 0b0011:
    mov_a_im(im);
    break;
  case 0b0100:
    mov_b_a(im);
    break;
  case 0b0101:
    add_b_im(im);
    break;
  case 0b0110:
    in_b(im);
    break;
  case 0b1001:
    out_b();
    break;
  case 0b1011:
    out_im(im);
    break;
  case 0b1110:
    jnc(im - 1);
    break;
  case 0b1111:
    jmp(im - 1);
    break;
  default:
    invalid();
    break;
  }
}

#endif
