package com.playground;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.*;import java.math.BigDecimal;import java.util.*;
@Service public class Simulator {
 public record Request(String symbol,BigDecimal price,long quantity,Instant executeAt,String requestId){}
 public record Order(UUID id,Request request,String status,Instant completedAt){}
 private final Map<String,Order> orders=new LinkedHashMap<>(); private final Clock clock;
 public Simulator(){this(Clock.systemUTC());} Simulator(Clock clock){this.clock=clock;}
 public synchronized Order create(Request r){
  if(r==null||r.symbol()==null||r.symbol().isBlank()||r.symbol().length()>30||r.price()==null||r.price().signum()<=0||r.price().stripTrailingZeros().scale()>0||r.quantity()<1||r.quantity()>1000000||r.executeAt()==null||r.requestId()==null||!r.requestId().matches("[a-zA-Z0-9-]{1,80}"))throw new IllegalArgumentException("اطلاعات سفارش معتبر نیست؛ قیمت و تعداد باید عدد صحیح مثبت باشند.");
  var existing=orders.get(r.requestId());if(existing!=null){if(!existing.request().equals(r))throw new IllegalArgumentException("شناسه تکراری برای سفارش متفاوت.");return existing;}
  var now=clock.instant();if(!r.executeAt().isAfter(now)||r.executeAt().isAfter(now.plusSeconds(86400)))throw new IllegalArgumentException("زمان باید در آینده و حداکثر تا ۲۴ ساعت دیگر باشد.");
  if(r.price().multiply(BigDecimal.valueOf(r.quantity())).compareTo(new BigDecimal("1000000000"))>0||orders.size()>=1000)throw new IllegalArgumentException("سقف مبلغ یک میلیارد ریال یا ظرفیت ۱۰۰۰ سفارش رد شده است.");
  var o=new Order(UUID.randomUUID(),r,"SCHEDULED",null);orders.put(r.requestId(),o);return o;
 }
 public synchronized List<Order> list(){return new ArrayList<>(orders.values());}
 public synchronized void stop(){orders.replaceAll((id,o)->o.status().equals("SCHEDULED")?new Order(o.id(),o.request(),"CANCELLED",clock.instant()):o);}
 @Scheduled(fixedDelay=100) public synchronized void tick(){var now=clock.instant();orders.replaceAll((id,o)->o.status().equals("SCHEDULED")&&!o.request().executeAt().isAfter(now)?new Order(o.id(),o.request(),"SIMULATED",now):o);}
}
