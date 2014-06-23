ftlMapper
=========

Simplifying usage of ftl-based sql templates with jdbc

Samples:

### Example 1
    public interface LolRepository {
        @Query("sql/lol/getAll.ftl")
        @MappedType(LolInfo.class)
        public List<LolInfo> getAll();
    }
    
### Example 2        
    public interface LolRepository2 {
        @Query("sql/lol/getOne.ftl")
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
