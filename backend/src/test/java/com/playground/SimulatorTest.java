package com.playground;
import org.junit.jupiter.api.Test;import java.time.*;import java.math.BigDecimal;import static org.assertj.core.api.Assertions.*;
class SimulatorTest {
 final Instant now=Instant.parse("2026-10-10T06:00:00Z");
 Simulator.Request req(String key,Instant at){return new Simulator.Request("فولاد",new BigDecimal("1000"),100,at,key);}
 @Test void deduplicatesAndCancels(){var s=new Simulator(Clock.fixed(now,ZoneOffset.UTC));var r=req("abc",now.plusSeconds(2));assertThat(s.create(r)).isEqualTo(s.create(r));assertThat(s.list()).hasSize(1);s.stop();s.tick();assertThat(s.list().get(0).status()).isEqualTo("CANCELLED");}
 @Test void rejectsPastTime(){var s=new Simulator(Clock.fixed(now,ZoneOffset.UTC));assertThatThrownBy(()->s.create(req("abc",now.minusSeconds(1)))).isInstanceOf(IllegalArgumentException.class);}
 @Test void executesOnceWhenDue(){class MutableClock extends Clock{Instant value=now;public ZoneId getZone(){return ZoneOffset.UTC;}public Clock withZone(ZoneId z){return this;}public Instant instant(){return value;}}var c=new MutableClock();var s=new Simulator(c);s.create(req("abc",now.plusSeconds(2)));s.tick();assertThat(s.list().get(0).status()).isEqualTo("SCHEDULED");c.value=now.plusSeconds(3);s.tick();var result=s.list().get(0);assertThat(result.status()).isEqualTo("SIMULATED");c.value=now.plusSeconds(5);s.tick();assertThat(s.list().get(0)).isEqualTo(result);}
}
