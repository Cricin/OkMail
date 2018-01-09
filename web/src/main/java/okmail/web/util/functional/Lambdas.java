package okmail.web.util.functional;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class Lambdas {

  public static <E> void forEach(E[] a, Consumer<E> consumer) {
    for (E e : a) {
      consumer.accept(e);
    }
  }

  public static <I, O> List<O> transform(List<I> input, Function<I, O> transformer) {
    List<O> out = new LinkedList<>();
    for (I e : input) {
      out.add(transformer.apply(e));
    }
    return out;
  }

  @SuppressWarnings("unchecked")
  public static <I, O> O[] transform(I[] input, Function<I, O> transformer) {
    List<O> out = new LinkedList<>();
    for (I e : input) {
      out.add(transformer.apply(e));
    }
    return (O[]) out.toArray();
  }

  public static <I> I test(List<I> input, Function<I, Boolean> tester) {
    if (input == null) return null;
    for (I i : input) {
      if (tester.apply(i)) return i;
    }
    return null;
  }

  public static <I> I test(I[] input, Function<I, Boolean> tester) {
    if (input == null) return null;
    for (I i : input) {
      if (tester.apply(i)) return i;
    }
    return null;
  }

}
