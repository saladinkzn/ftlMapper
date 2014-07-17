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
        @Column
        public long id;
        @Column        
        public String name;
        
        public LolInfo() {}
    }
    
### Example 2        
    public interface LolRepository2 {
        @Template("sql/lol/getOne.ftl")
        @Mapper(LolInfoMapper.class)
        public LolInfo getOne(@Param("id") id);
    }
    
    public class LolInfoMapper extends ResultSetExtractor<LolInfo> {
        public LolInfo mapRow(ResultSet resultSet) {
            if(resultSet.next()) {
                final long id = resultSet.getLong("id");
                final String name = resultSet.getString("name");
                return new LolInfo(id, name);
            } else {
                return null;
            }
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
        public LolInfo(@Column("id") long id, @Column("name") String name) {
            this.id = id;
            this.name = name;
        }
    }