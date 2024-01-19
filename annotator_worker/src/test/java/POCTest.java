import lombok.extern.slf4j.Slf4j;

@Slf4j
public class POCTest {
//    private static Flux<List<ExObj>> processBatch(final List<ExampleObject> objects) {
//        log.info("Batch: {}", objects);
//        return Flux.just(objects.stream()
//                .map(obj -> new ExObj(-obj.id))
//                .toList());
//    }

//    @Test
//    public void deser() throws IOException {
//        final var messge = """
//                {
//                  "annotationRequestId": {
//                    "uuid": "3f612ac8-8090-49d1-b478-afe63c8217fa"
//                  },
//                  "variant": {
//                    "chromosome": "chromosome_997ad9690b5b",
//                    "position": 0,
//                    "referenceAllele": {
//                      "nucleotides": [
//                        "A"
//                      ]
//                    },
//                    "alternativeAllele": {
//                      "nucleotides": [
//                        "A"
//                      ]
//                    },
//                    "type": "SNV"
//                  },
//                  "algorithm": "PANGOLIN"
//                }
//                """;
//        final byte[] data = {123, 34, 97, 110, 110, 111, 116, 97, 116, 105, 111, 110, 73, 100, 34, 58, 123, 34, 105, 100, 34, 58, 34, 52, 51, 51, 52, 49, 55, 55, 51, 45, 53, 98, 102, 53, 45, 52, 97, 98, 54, 45, 97, 101, 100, 48, 45, 54, 57, 56, 52, 49, 48, 99, 57, 99, 53, 50, 98, 34, 125, 44, 34, 118, 97, 114, 105, 97, 110, 116, 34, 58, 123, 34, 99, 104, 114, 111, 109, 111, 115, 111, 109, 101, 34, 58, 34, 49, 55, 34, 44, 34, 112, 111, 115, 105, 116, 105, 111, 110, 34, 58, 52, 49, 50, 55, 54, 49, 51, 53, 44, 34, 114, 101, 102, 101, 114, 101, 110, 99, 101, 65, 108, 108, 101, 108, 101, 34, 58, 123, 34, 98, 108, 97, 110, 107, 34, 58, 102, 97, 108, 115, 101, 125, 44, 34, 97, 108, 116, 101, 114, 110, 97, 116, 105, 118, 101, 65, 108, 108, 101, 108, 101, 34, 58, 123, 34, 98, 108, 97, 110, 107, 34, 58, 102, 97, 108, 115, 101, 125, 44, 34, 116, 121, 112, 101, 34, 58, 34, 83, 78, 86, 34, 44, 34, 115, 110, 118, 34, 58, 116, 114, 117, 101, 44, 34, 105, 110, 100, 101, 108, 34, 58, 102, 97, 108, 115, 101, 125, 44, 34, 97, 108, 103, 111, 114, 105, 116, 104, 109, 34, 58, 34, 80, 65, 78, 71, 79, 76, 73, 78, 34, 125};
//        final var mapper = new ObjectMapper();
//        final var result = 'a';
////                mapper.readValue(data, AnnotationRequest.class);
//        log.info("Res {}", result);
//    }
//
//    @Test
//    void shouldGroupFlux() {
//        Flux.interval(Duration.ofMillis(500))
//                .map(ExampleObject::new)
//                .groupBy(obj -> obj.id % 2)
//                .flatMap(group ->
//                        group.buffer(3) // Buffering in batches of 10
//                                .flatMap(POCTest::processBatch)
//                )
//                .subscribe(System.out::println);
//
//        try {
//            Thread.sleep(10000); // for example, wait 10 seconds
//        } catch (final InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    @Test
//    void shouldSaveFile() {
//        final CsvHandler handler = new CsvHandler();
//        handler.saveBean(Path.of("/home/mszawerd/test"), new ArrayList<>(List.of(new PangolinInput("a", "b", 1, "c", "d"))));
//    }
//
//    private record ExampleObject(long id) {
//    }
//
//    private record ExObj(long id) {

//    }
}
