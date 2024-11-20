package zad1;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XList<Type> extends ArrayList<Type> {
    public XList() {
        super();
    }

    public XList(Collection<? extends Type> c) {
        super(c);
    }

    public XList(Type... elements) {
        super(Arrays.asList(elements));
    }

    public static <Type> XList<Type> of(Collection<? extends Type> c) {
        return new XList<>(c);
    }

    public static <Type> XList<Type> of(Type... elements) {
        return new XList<>(elements);
    }

    public static XList<String> charsOf(String words) {
        return words.chars().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.toCollection(XList::new));
    }

    public static XList<String> tokensOf(String words) {
        return tokensOf(words, "\\s+");
    }

    public static XList<String> tokensOf(String words, String delimiter) {
        return new XList<>(words.split(delimiter));
    }

    public XList<Type> union(Collection<? extends Type> collection) {
        XList<Type> temp = new XList<>(this);
        temp.addAll(collection);
        return temp;
    }

    public XList<Type> union(Type... collection) {
        return union(Arrays.asList(collection));
    }

    public XList<Type> diff(Collection<? extends Type> collection) {
        XList<Type> temp = new XList<>(this);
        temp.removeAll(collection);
        return temp;
    }

    public XList<Type> unique() {
        return this.stream().distinct().collect(Collectors.toCollection(XList::new));
    }

    public <T> XList<T> collect(Function<? super Type, ? extends T> mapper) {
        return this.stream().map(mapper).collect(Collectors.toCollection(XList::new));
    }

    public String join() {
        return join("");
    }

    public String join(String delimiter) {
        return this.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    public void forEachWithIndex(BiConsumer<? super Type, Integer> action) {
        for (int i = 0; i < this.size(); i++) {
            action.accept(this.get(i), i);
        }
    }

    public XList<XList<Type>> combine() {
        List<XList<Type>> result = new ArrayList<>();
        combine(result, this.size() - 1, new XList<>());
        result.forEach(Collections::reverse);
        return new XList<>(result);
    }

    private void combine(List<XList<Type>> result, int level, XList<Type> current) {
        if (level == -1) {
            result.add(current);
            return;
        }
        List<Type> flat = ((List<Type>) this.get(level));
        for (Type element : flat) {
            XList<Type> newList = new XList<>(current);
            newList.add(element);
            combine(result, level - 1, newList);
        }
    }
}
