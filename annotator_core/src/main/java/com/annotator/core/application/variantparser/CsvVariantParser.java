package com.annotator.core.application.variantparser;

import com.annotator.core.domain.annotation.Variant;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CsvVariantParser implements VariantParser {
    @Override
    public List<Variant> read(final InputStream stream) {
        final List<Variant> variants = new ArrayList<>();
        try (final Reader reader = new BufferedReader(new InputStreamReader(stream))) {
            reader.mark(1);
            if (reader.read() != 0xFEFF) {
                reader.reset(); // Not a BOM, reset to start of stream
            }

            try (final CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder()
                            .withSeparator('\t')
                            .build()
                    ).build()) {

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
        } catch (final Exception e) {
            throw new InvalidVariantFormat("Can't parse this variant file", e);
        }
        return variants;
    }

    private record Header(int chromIdx, int posIdx, int refIdx, int altIdx, int genIdx) {
        public static Header from(final String[] row) {
//            Objects.requireNonNull(row);
            final List<String> header = Arrays.stream(row).collect(Collectors.toList());

            return new Header(
                    findColumnIndex(header, "Chr"),
                    findColumnIndex(header, "POS"),
                    findColumnIndex(header, "Ref"),
                    findColumnIndex(header, "Alt"),
                    findColumnIndex(header, "HGVS")
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
