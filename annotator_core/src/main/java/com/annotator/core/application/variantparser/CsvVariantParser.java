package com.annotator.core.application.variantparser;

import com.annotator.core.domain.Variant;
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
    public List<Variant> read(InputStream stream) {
        List<Variant> variants = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(stream))) {
            Header header = Header.from(csvReader.readNext());

            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Variant variant = new Variant.Builder()
                        .chromosome(row[header.chromIdx])
                        .position(Long.parseLong(row[header.posIdx]))
                        .referenceAllele(row[header.refIdx])
                        .alternativeAllele(row[header.altIdx])
                        .gene(row[header.genIdx])
                        .build();

                variants.add(variant);
            }
        } catch (Exception e) {
            throw new InvalidVariantFormat("Can't parse this variant file", e);
        }

        return variants;
    }

    private record Header(int chromIdx, int posIdx, int refIdx, int altIdx, int genIdx) {
        public static Header from(String[] row) {
            Objects.requireNonNull(row);
            List<String> header = Arrays.stream(row).collect(Collectors.toList());

            return new Header(
                    findColumnIndex(header, "#CHROM"),
                    findColumnIndex(header, "POS"),
                    findColumnIndex(header, "REF"),
                    findColumnIndex(header, "ALT"),
                    findColumnIndex(header, "GENE")
            );
        }

        private static int findColumnIndex(List<String> header, String columnName) {
            int idx = header.indexOf(columnName);
            if (idx == -1) {
                throw new InvalidVariantFormat(String.format("Column name %s doesn't exist", columnName));
            }

            return idx;
        }
    }
}
