ftlMapper
=========

Simplifying usage of ftl-based sql templates with jdbc

Samples:

    public interface LolRepository {
        @Query("sql/lol/getAll.ftl")
        @MappedType(LolInfo.class)
        public List<LolInfo> getAll();
    }
