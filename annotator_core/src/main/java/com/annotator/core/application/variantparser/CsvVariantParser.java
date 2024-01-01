package com.annotator.core.application.variantparser;

import com.annotator.core.domain.annotation.Variant;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CsvVariantParser implements VariantParser {
    @Override
    public List<Variant> read(final InputStream stream) {
        final List<Variant> variants = new ArrayList<>();

        try (final CSVReader csvReader = new CSVReader(new InputStreamReader(stream))) {
            final Header header = Header.from(csvReader.readNext());

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                final Variant variant = Variant.builder()
                        .chromosome(row[header.chromIdx])
                        .position(Long.parseLong(row[header.posIdx]))
                        .referenceAllele(row[header.refIdx])
                        .alternativeAllele(row[header.altIdx])
                        .gene(row[header.genIdx])
                        .build();

                variants.add(variant);
            }
        } catch (final Exception e) {
            throw new InvalidVariantFormat("Can't parse this variant file", e);
        }

        return variants;
    }

    private record Header(int chromIdx, int posIdx, int refIdx, int altIdx, int genIdx) {
        public static Header from(final String[] row) {
            Objects.requireNonNull(row);
            final List<String> header = Arrays.stream(row).collect(Collectors.toList());

            return new Header(
                    findColumnIndex(header, "#CHROM"),
                    findColumnIndex(header, "POS"),
                    findColumnIndex(header, "REF"),
                    findColumnIndex(header, "ALT"),
                    findColumnIndex(header, "GENE")
            );
        }

        private static int findColumnIndex(final List<String> header, final String columnName) {
            final int idx = header.indexOf(columnName);
            if (idx == -1) {
                throw new InvalidVariantFormat(String.format("Column name %s doesn't exist", columnName));
            }

            return idx;
        }
    }
}
