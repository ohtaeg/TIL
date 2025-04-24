package optional;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;

/**
 * 예열을 위한 어노테이션, 2회 반복, 예열하는 이유는 JVM이 처음에는 인터프리터 방식으로 진행하다가 자주 실행되는 코드는 인터프리터로 실행하지 않고 JIT 컴파일러로 최적화를 수행하기 때문에
 * 최적화 전과 최적화 후의 성능이 다를 수 있어 예열된 상태로 최적화된 상태에서 일관된 성능 측정을 위해 사용
 */
@Fork(value = 2)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
public class OptionalBenchmark {

    private static final long BILLION = 1_000_000_000L;

    // 성능 측정에 대상이 될 메소드 지정
    @Benchmark
    // 성능 측정 결과를 출력할 시간 단위 지정
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    /*
      @param blackhole => 실행된 코드의 결과를 반환하지 않거나 사용하지 않으면 데드코드로 인식하기때문에 성능 측정에 영향을 미치는 것을 최소화
     */
    public long sumLong(Blackhole blackhole) {
        long sum = 0L;
        for (long i = 1L; i <= BILLION; i++) {
            sum += i;
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long sumOptional(Blackhole blackhole) {
        long sum = 0L;
        for (long i = 1L; i <= BILLION; i++) {
            Optional<Long> n = Optional.ofNullable(i);
            sum += n.orElse(0L);
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long sumOptionalPrimitive(Blackhole blackhole) {
        long sum = 0L;
        for (long i = 1L; i <= BILLION; i++) {
            OptionalLong n = OptionalLong.of(i);
            sum += n.orElse(0L);
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long sumLongWithNull(Blackhole blackhole) {
        long sum = 0L;
        for (long i = 1L; i <= BILLION; i++) {
            Long n = getNullOrLong(i);
            if (n != null) {
                sum += n;
            }
        }
        blackhole.consume(sum);
        return sum;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long sumOptionalNulls(Blackhole blackhole) {
        long sum = 0L;
        for (long i = 1L; i <= BILLION; i++) {
            Optional<Long> n = Optional.ofNullable(getNullOrLong(i));
            sum += n.orElse(0L);
        }
        blackhole.consume(sum);
        return sum;
    }

    public Long getNullOrLong(long i) {
        return i % 3 == 0 ? i : null;
    }
}
