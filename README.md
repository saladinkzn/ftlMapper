ftlMapper
=========

Simplifying usage of ftl-based sql templates with jdbc

Samples:

### Example 1
    public interface LolRepository {
        @Template("sql/lol/getAll.ftl")
        public List<LolInfo> getAll();
    }
    
    @MappedType
    public class LolInfo {
        @Property
        private long id;
        @Property        
        private String name;
        
        public LolInfo() {}
    }
    
### Example 2        
    public interface LolRepository2 {
        @Template("sql/lol/getOne.ftl")
        @Mapper(LolInfoMapper.class)
        public LolInfo getOne(@Param("id") id);
    }
    
    public class LolInfoMapper extends RowMapper<LolInfo> {
        public LolInfo mapRow(ResultSet resultSet) {
            final long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            return new LolInfo(id, name);
        }
    }
    
### Example 3
    public interface LolRepository3 {
        @Query("select id, name from lol where id = ?1")
        public LolInfo getOne(@Param("id") long id);
    }

    @MappedType
    public class LolInfo {
        private final long id;
        private final String name;

        @Creator
        public LolInfo(@Property("id") long id, @Property("name") String name) {
            this.id = id;
            this.name = name;
        }
    }