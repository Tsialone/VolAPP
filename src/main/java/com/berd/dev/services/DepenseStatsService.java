package com.berd.dev.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.berd.dev.dtos.RecapPointDto;
import com.berd.dev.dtos.RecapResponseDto;
import com.berd.dev.models.Depense;
import com.berd.dev.repositories.DepenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepenseStatsService {

    private static final DateTimeFormatter JOUR_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter SHORT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM");
    private static final DateTimeFormatter MOIS_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);

    private final DepenseRepository depenseRepository;

    public RecapResponseDto getRecapStats(String periode) {
        String periodeNormalisee = normalizePeriode(periode);

        List<Depense> depenses = depenseRepository.findAll().stream()
                .filter(depense -> depense.getCreated() != null)
                .sorted(Comparator.comparing(Depense::getCreated))
                .toList();

        Map<String, List<Double>> grouped = new LinkedHashMap<>();

        for (Depense depense : depenses) {
            String label = toPeriodLabel(depense.getCreated(), periodeNormalisee);
            double totalDepense = calculateMontantDepense(depense);
            grouped.computeIfAbsent(label, k -> new ArrayList<>()).add(totalDepense);
        }

        List<RecapPointDto> points = grouped.entrySet().stream()
                .map(entry -> new RecapPointDto(entry.getKey(), average(entry.getValue())))
                .toList();

        double moyenneGlobale = average(points.stream().map(RecapPointDto::depenseMoyenne).toList());

        Optional<RecapPointDto> maxPoint = points.stream()
                .max(Comparator.comparingDouble(RecapPointDto::depenseMoyenne));

        return new RecapResponseDto(
                periodeNormalisee,
                points,
                moyenneGlobale,
                maxPoint.map(RecapPointDto::label).orElse("-"),
                maxPoint.map(RecapPointDto::depenseMoyenne).orElse(0.0));
    }

    private String normalizePeriode(String periode) {
        if (periode == null || periode.isBlank()) {
            return "mensuelle";
        }

        String value = periode.toLowerCase(Locale.ROOT).trim();
        return switch (value) {
            case "journaliere", "journalier", "jour", "daily" -> "journaliere";
            case "hebdomadaire", "hebdo", "weekly" -> "hebdomadaire";
            case "mensuelle", "mensuel", "mois", "monthly" -> "mensuelle";
            case "annuelle", "annuel", "an", "yearly" -> "annuelle";
            default -> "mensuelle";
        };
    }

    private String toPeriodLabel(LocalDateTime created, String periode) {
        return switch (periode) {
            case "journaliere" -> created.toLocalDate().format(JOUR_FORMAT);
            case "hebdomadaire" -> {
                LocalDate monday = created.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate sunday = created.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                yield monday.format(SHORT_DATE_FORMAT) + " - " + sunday.format(SHORT_DATE_FORMAT);
            }
            case "annuelle" -> String.valueOf(created.getYear());
            default -> capitalizeMonth(created.format(MOIS_FORMAT));
        };
    }

    private String capitalizeMonth(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return value.substring(0, 1).toUpperCase(Locale.FRENCH) + value.substring(1);
    }

    private double calculateMontantDepense(Depense depense) {
        if (depense.getDepenseDetails() == null || depense.getDepenseDetails().isEmpty()) {
            return 0.0;
        }
        return depense.getDepenseDetails().stream()
                .mapToDouble(detail -> (detail.getQte() != null ? detail.getQte() : 0.0)
                        * (detail.getPu() != null ? detail.getPu() : 0.0))
                .sum();
    }

    private double average(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
