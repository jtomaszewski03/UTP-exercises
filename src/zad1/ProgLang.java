package zad1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgLang {
    private final Map<String, List<String>> langsMap;
    private final Map<String, List<String>> progsMap;

    public ProgLang(String fname) {
        langsMap = new LinkedHashMap<>();
        progsMap = new LinkedHashMap<>();
        try(Stream<String> lines = Files.lines(Paths.get(fname))) {
            lines.forEach(line -> {
                String[] split = line.split("\t");
                String lang = split[0];
                List<String> progs = Arrays.asList(split).subList(1, split.length).stream()
                        .distinct().collect(Collectors.toList());
                langsMap.put(lang, progs);
                for (String prog : progs) {
                    progsMap.computeIfAbsent(prog, k -> new ArrayList<>()).add(lang);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<String>> getLangsMap() {
        return langsMap;
    }

    public Map<String, List<String>> getProgsMap() {
        return progsMap;
    }

    public Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(langsMap, Comparator.comparing((Map.Entry<String, List<String>> entry) -> entry.getValue().size())
                .reversed().thenComparing(Map.Entry::getKey));
    }

    public Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(progsMap, Comparator.comparing((Map.Entry<String, List<String>> entry) -> entry.getValue().size())
                .reversed().thenComparing(Map.Entry::getKey));
    }

    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int numOfLangs) {
        return filtered(progsMap, e -> e.getValue().size() > numOfLangs);
    }

    public <Type, Another> Map<Type, Another> sorted(Map<Type, Another> map, Comparator<Map.Entry<Type, Another>> comparator) {
        return map.entrySet().stream().sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public <Type, Another> Map<Type, Another> filtered(Map<Type, Another> map, Predicate<Map.Entry<Type, Another>> predicate) {
        return map.entrySet().stream().filter(predicate)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }
}
