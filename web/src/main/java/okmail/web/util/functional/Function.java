package okmail.web.util.functional;

@FunctionalInterface
public interface Function<I, O> {
  O apply(I i);
}
