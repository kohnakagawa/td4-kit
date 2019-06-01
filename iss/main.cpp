#include <iostream>
#include <vector>
#include <fstream>
#include <unordered_map>
#include <functional>
#include <stdexcept>
#include <bitset>
#include <sys/stat.h>

#include "helper.hpp"

struct uint4x2_t {
  uint8_t lo : 4;
  uint8_t hi : 4;
  uint4x2_t() : lo(0), hi(0) {}
};

union Inst {
  uint8_t raw;
  uint4x2_t code;
};

using Reg = uint4x2_t;
using Port = uint4x2_t;
using Flag = bool;

class CPU {
  #include "insts.hpp"

  Reg a_, b_;
  Port in_, out_;
  Reg pc_;
  Flag cflag_;

  std::vector<uint8_t> inst_code_;

public:
  CPU(const uint8_t* code,
      const std::size_t size) : inst_code_(code, code + size) {}
  CPU(const std::vector<uint8_t>& code) : inst_code_(code) {}
  ~CPU() {}

  void clock() {
    const Inst inst {inst_code_[pc_.lo]};
    execute(inst);
    pc_.lo++;
  }

  void show_port() {
    std::cout << std::bitset<4>(out_.lo) << std::endl;
  }

  void run() {
    try {
      while (1) {
        clock();
        show_port();
      }
    } catch (const std::runtime_error& err) {
      std::cerr << err.what() << std::endl;
    }
  }
};

int main(const int argc, const char* argv[]) {
  if (argc != 2) {
    std::cerr << "Usage:\n";
    std::cerr << argv[0] << " binary" << std::endl;
    return EXIT_FAILURE;
  }

  std::ifstream fin(argv[1], std::ios::in | std::ios::binary);
  if (!fin) {
    std::cerr << "Cannot open " << argv[1] << std::endl;
    return EXIT_FAILURE;
  }

  struct stat st;
  if (stat(argv[1], &st) != 0) {
    std::cerr << "Cannot open " << argv[1] << std::endl;
    return EXIT_FAILURE;
  }
  const auto file_size = st.st_size;

  std::vector<uint8_t> buffer(file_size, 0);
  fin.read(reinterpret_cast<char*>(buffer.data()), file_size);

  CPU cpu(buffer);
  cpu.run();
}
