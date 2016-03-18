class RandomInputGenerator

  def initialize(args)
    particle_count, seed = 123, grid_size = 20, max_radious = 1
    @particle_count = args[0]
    @grid_size = args[2].to_i || 20
    @max_radious = args[3].to_f || 1
    seed = args[1].to_i || 123
    @dynamic_file = File.open("Dynamic#{@particle_count}.txt", 'w+')
    @static_file = File.open("Static#{@particle_count}.txt", 'w+')
    @random = Random.new(seed)
  end

  def generate
    @dynamic_file.puts "\t0"
    @static_file.puts "\t#{@particle_count}"
    @static_file.puts "\t#{@grid_size}"
    @particle_count.to_i.times do |i|
      @dynamic_file.puts "\t#{random_position}\t#{random_position}"
      @static_file.puts "\t#{random_radious}\t1"
    end
    @dynamic_file.close
    @static_file.close
  end


  private

  def random_position
    @random.rand(0.0..@grid_size.to_f)
  end

  def random_radious
    @random.rand(0.0..@max_radious.to_f)
  end
end
