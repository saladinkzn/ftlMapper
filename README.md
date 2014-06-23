ftlMapper
=========

Simplifying usage of ftl-based sql templates with jdbc

Samples:

### Example 1
    public interface LolRepository {
        @Template("sql/lol/getAll.ftl")
        @MappedType(LolInfo.class)
        public List<LolInfo> getAll();
    }
    
### Example 2        
    public interface LolRepository2 {
        @Template("sql/lol/getOne.ftl")
        @MappedType(LolInfoMapper.class)
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
        @MappedType(LolInfo.class)
        public LolInfo getOne(@Param("id") long id);
    }
