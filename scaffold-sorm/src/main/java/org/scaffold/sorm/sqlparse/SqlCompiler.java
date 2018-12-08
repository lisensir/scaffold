package org.scaffold.sorm.sqlparse;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.scaffold.sorm.utils.*;
import org.scaffold.sorm.annotation.*;
import org.scaffold.sorm.dialect.Dialect;
import org.scaffold.sorm.dialect.MysqlDialect;
import org.springframework.util.ObjectUtils;


/**
 * 将带有<code>${}</code>表达式的Sql语句编译成为SpringJdbc可执行的形式.
 *
 * @author lisen 2016/8/29
 */
public class SqlCompiler {

    private static Logger logger = Logger.getLogger(SqlCompiler.class);

    private static Map<String, String> COMPILE_SQL_C = new ConcurrentHashMap<>();


    /**
     * 保存编译后的sql及参数
     */
    public static class QueryStruct {

        private Map<String, ?> param;

        private String sql;

        public QueryStruct(Map<String, ?> paramMap, String sql) {

            this.sql = sql;
            this.param = paramMap;
        }

        public Map<String, ?> getParam() {

            return this.param;
        }

        public String getSql() {

            return this.sql;
        }

        public void setParam(Map<String, Object> param) {

            this.param = param;
        }

        public void setSql(String sql) {

            this.sql = sql;
        }
    }


    /**
     * 使用正则表达式将带有${}标签的sql根据查询条件编译成原始sql
     * <p>
     * 如输入查询条件 Map paramMap = new HashMap(); paramMap.put("ertuMemo", "234");
     * 和sql="select * from trun where 1=1 ${ and t1.ertuMemo like :ertuMemo}";
     * 将转换成"select * from trun where 1=1 and t1.ertuMemo like :ertuMemo "
     * <p>
     * 如输入查询条件 Map paramMap = new HashMap(); paramMap.put("foo", "234");
     * 将转换成"select * from trun where 1=1 "
     * </p>
     * 注：<b>如果是Like语句，将自动进行全模糊匹配</b>
     *
     * @param paramMap 参数Map
     * @param sql      使用：号占位符的sql
     * @return QueryStruct
     */
    @SuppressWarnings("rawtypes")
    public static QueryStruct compileQuery(Map<String, ?> paramMap, String sql) {

        Matcher mTag = RegexUtil.pTag.matcher(sql);
        if (ObjectUtils.isEmpty(paramMap)) {
            return new QueryStruct(paramMap, mTag.replaceAll(""));
        }
        if (!mTag.find()) {
            return new QueryStruct(paramMap, sql);
        }
        mTag.reset();

        Map<String, Object> conMap = new HashMap<>();
        StringBuffer bufTag = new StringBuffer();

        while (mTag.find()) {
            String tag = mTag.group();
            Matcher mCon = RegexUtil.pCon.matcher(tag);
            if (mCon.find()) {
                String tabAlias = mCon.group(1);
                String colName = mCon.group(2);
                String opType = mCon.group(3);
                String keyName = mCon.group(4);

                Matcher mDate = RegexUtil.pDate.matcher(colName);
                Matcher mDtime = RegexUtil.pDtime.matcher(colName);
                Matcher mDtimeFn = RegexUtil.pDtimeFn.matcher(tag);
                Object objKey = paramMap.get(keyName);// 参数值

                if (objKey == null) {
                    mTag.appendReplacement(bufTag, "");
                    continue;
                }

                if (objKey instanceof String) {//去掉字符串左右空格
                    objKey = objKey.toString().trim();
                }

                String strReplace = StringUtils.substring(tag, 2, tag.length() - 1);

                String paramValue;
                paramValue = objKey.toString();
                boolean isNotCollection = (objKey instanceof Date)
                        || ((objKey instanceof String) && StringUtils.isNotEmpty(paramValue))
                        || org.apache.commons.lang3.math.NumberUtils.isNumber(paramValue);
                boolean isValidCollection = (objKey instanceof Collection) && !((Collection) objKey).isEmpty();

                if (isNotCollection) {

                    if (mDate.find()) {

                        if (objKey instanceof Date) {
                            conMap.put(keyName, DateUtil.format((Date) objKey, DateUtil.DATE_DAY_FORMAT));// Date转字符串
                        } else {
                            conMap.put(keyName, objKey);
                        }

                        if (!mDtimeFn.find()) {// 没有处理的日期
                            String longColName = tabAlias + "." + colName;
                            String subst = getDialect().date2Char(longColName);
                            strReplace = StringUtils.replace(strReplace, longColName, subst);
                        }
                    } else if (mDtime.find()) {
                        if (objKey instanceof Date) {//
                            conMap.put(keyName, DateUtil.format((Date) objKey, DateUtil.DATE_DAYTIME_FORMAT));// Date转字符串
                        } else {
                            conMap.put(keyName, objKey);
                        }

                        if (!mDtimeFn.find()) {// 没有处理的日期
                            String longColName = tabAlias + "." + colName;
                            String subst = getDialect().datetime2Char(longColName);
                            strReplace = StringUtils.replace(strReplace, longColName, subst);
                        }
                    } else {

                        if (opType.equalsIgnoreCase("like") && !((paramValue).startsWith("%") || (paramValue).endsWith("%"))) {
                            conMap.put(keyName, objKey);
                        } else if (opType.equalsIgnoreCase("in")) { //IN语句时如果参数是字符串则自动转化成Set
                            conMap.put(keyName, org.springframework.util.StringUtils.commaDelimitedListToSet(paramValue));
                        } else {
                            conMap.put(keyName, objKey);
                        }
                    }

                    mTag.appendReplacement(bufTag, strReplace);

                } else if (isValidCollection) {
                    conMap.put(keyName, objKey);
                    mTag.appendReplacement(bufTag, strReplace);
                } else {
                    mTag.appendReplacement(bufTag, "");
                }
            } else {
                mTag.appendReplacement(bufTag, "");
            }
        }

        mTag.appendTail(bufTag);

        String compiledSql = bufTag.toString().replaceAll("\\s+", " ");

        if (logger.isDebugEnabled()) {
            logger.debug("##编译后的sql: " + compiledSql);
            logger.debug("查询参数[" + paramMap + "]");
        }

        //COMPILE_SQL_C.put(sql, compiledSql);

        return new QueryStruct(conMap, compiledSql);
    }

    private static Map<String,String> COUNT_SQL_C = new ConcurrentHashMap<>();

    /**
     * 通过查询sql获取统计行数的sql
     *
     * @param sql
     * @return
     */
    public static String getCountSql(String sql) {

        if(COMPILE_SQL_C.containsKey(sql)) {
            return SqlCompiler.COMPILE_SQL_C.get(sql);
        }

        String temp_sql = sql;

        String[] temp = temp_sql.split("\\s");

        StringBuilder sb = new StringBuilder();

        int i = 0;
        boolean flag = false;
        String countStr = " count(*) ";
        for (String str : temp) {
            if (flag) {
                if (StringUtils.isNotBlank(str))
                    sb.append(str).append(" ");
                continue;
            }
            if (i == 0 && str.equalsIgnoreCase("select")) {
                sb.append(str).append(" ");
                i++;
            } else if (i > 0
                    && (str.equalsIgnoreCase("(select") || str.equalsIgnoreCase("select"))) {
                i++;
            }
            if (str.equalsIgnoreCase("from")) {
                i--;
                if (i == 0) {
                    sb.append(countStr).append(" from ");
                    flag = true;
                }
            }
        }

        String countSql = sb.toString()
                .replaceAll("order.*\\)", ")")
                .replaceAll("order.*", "")
                .replaceAll("\\s+", " ")
                .replaceAll("\\s+\\)", ")")
                .trim();

        if (logger.isDebugEnabled()) {
            logger.debug("##统计总记录数sql: " + countSql);
        }

        SqlCompiler.COMPILE_SQL_C.put(sql, countSql);

        return countSql;
    }


    private static Map<String,String> INSERT_SQL_C = new ConcurrentHashMap<>();

    /**
     * 生成Insert语句
     *
     * @param beanClass 实体类型
     * @return String
     */
    public static <T> String getInsertSql(Class<T> beanClass) throws SqlCompileException {

        if(SqlCompiler.INSERT_SQL_C.containsKey(beanClass.getName())) {
            return SqlCompiler.INSERT_SQL_C.get(beanClass.getName());
        }

        String tableName = getTableName(beanClass);

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO " + tableName);

        PropertyDescriptor[] pdArr = BeanUtil.getPropertyDescriptors(beanClass);
        StringBuilder var1 = new StringBuilder();
        StringBuilder var2 = new StringBuilder();

        try {
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getName().equals("class")) continue;
                Field f = BeanUtil.getField(beanClass, pd.getName());
                if (f.getDeclaredAnnotation(NotPersist.class) != null) continue;

                Persist p = f.getDeclaredAnnotation(Persist.class);
                if (p != null && !p.name().trim().equals("")) {
                    var1.append(p.name()).append(",");
                } else {
                    var1.append(toDBName(pd.getName())).append(",");
                }

                var2.append(":").append(pd.getName()).append(",");
            }
        } catch (NoSuchFieldException e) {
            throw new SqlCompileException("生成insert语句异常：" + e.getMessage(), e);
        }

        String var3 = var1.toString().substring(0, var1.toString().length() - 1);
        String var4 = var2.toString().substring(0, var2.toString().length() - 1);

        sql.append("(" + var3 + ")");
        sql.append(" VALUES (");
        sql.append(var4 + ")");

        if (logger.isDebugEnabled()) {
            logger.debug("##系统生成Insert语句：" + sql.toString());
        }

        SqlCompiler.INSERT_SQL_C.put(beanClass.getName(), sql.toString());

        return sql.toString();
    }

    /**
     * 通过bean类型信息获取表名.
     * <p>
     * 如果在使用了<code>@Table(name='xxx')</code>注解则已注解指定的
     * 表名为准，否则通过默认的转换规则获取
     * </p>
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> String getTableName(Class<T> beanClass) {
        Table table = beanClass.getDeclaredAnnotation(Table.class);
        String tableName;
        if (table != null)
            tableName = table.name();
        else {
            tableName = toDBName(beanClass.getSimpleName());
        }
        return tableName;
    }


    /**
     * 通过数据体类获取更新语句.
     * 如果Bean包含唯一约束字段，则会依据唯一约束字段生成update语句，（唯一约束字段具有业务含义）
     * 如果Bean没有包含唯一约束字段，但存在Key，则会依据Key来生成update语句，（Key字段可能是表的流水号，不一定有业务含义）
     *
     * @param beanClass beanClass
     * @return String
     * @throws SqlCompileException
     */
    public static <T> String getUpdateSql(Class<T> beanClass) throws SqlCompileException {
        return _getUpdateSql(beanClass, null);
    }


    /**
     * 通过数据体类获取更新语句.
     * 如果Bean包含唯一约束字段，则会依据唯一约束字段生成update语句，（唯一约束字段具有业务含义）
     * 如果Bean没有包含唯一约束字段，但存在Key，则会依据Key来生成update语句，（Key字段可能是表的流水号，不一定有业务含义）
     *
     * @param beanClass     beanClass
     * @param specifiedProp 指定参与更新的字段
     * @return
     * @throws SqlCompileException
     */
    public static <T> String getUpdateSql(Class<T> beanClass, String[] specifiedProp) throws SqlCompileException {
        return _getUpdateSql(beanClass, specifiedProp);
    }

    private static Map<String, String> UPDATE_SQL_C = new ConcurrentHashMap<>();

    /**
     * 构建update语句.
     *
     * @param beanClass     beanClass
     * @param specifiedProp 指定需要更新的字段，其他字段不会更新
     * @return String
     * @throws SqlCompileException
     */
    private static <T> String _getUpdateSql(Class<T> beanClass, String[] specifiedProp) throws SqlCompileException {

        String sps = ObjectUtils.isEmpty(specifiedProp) ? "" : Arrays.deepToString(specifiedProp);
        if(SqlCompiler.UPDATE_SQL_C.containsKey(beanClass.getName()+sps)) {
            return SqlCompiler.UPDATE_SQL_C.get(beanClass.getName()+sps);
        }

        String tableName = getTableName(beanClass);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE " + tableName + " SET ");

        PropertyDescriptor[] pdArr = BeanUtil.getPropertyDescriptors(beanClass);
        StringBuilder setVar = new StringBuilder();
        StringBuilder whereVar = new StringBuilder();

        /*
         * 如果Bean包含唯一约束字段，则会依据唯一约束字段生成update语句，（唯一约束字段具有业务含义）
         * 如果Bean没有包含唯一约束字段，但存在Key，则会依据Key来生成update语句，（Key字段可能是表的流水号，不一定有业务含义）
         */
        boolean hasUniqueConstraint = hasUniqueConstraint(beanClass, pdArr);

        try {
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getName().equals("class")) continue;
                Field f = BeanUtil.getField(beanClass, pd.getName());
                Persist persist = f.getDeclaredAnnotation(Persist.class);
                Key key = f.getDeclaredAnnotation(Key.class);
                Unique unique = f.getDeclaredAnnotation(Unique.class);

                if (f.getDeclaredAnnotation(NotPersist.class) != null) continue;

                /*
                 * 设置update的Where部分，如果Bean中指定了唯一约束且当前的属性指定了唯一约束在优先考虑使用唯一约束，
                 * 否则使用指定为Key的属性生成
                 */
                if ((hasUniqueConstraint && unique != null) || (!hasUniqueConstraint && key != null)) {
                    String collName;
                    if (persist != null && !persist.name().trim().equals("")) {
                        collName = persist.name();
                    } else {
                        collName = toDBName(pd.getName());
                    }
                    if (whereVar.length() == 0) {
                        whereVar.append(" WHERE ").append(collName).append("=:").append(pd.getName());
                    } else {
                        whereVar.append(" AND ").append(collName).append("=:").append(pd.getName());
                    }
                    continue;
                }

                //如果指定了唯一约束，则不考虑指定为Key的属性，直接跳过
                if (hasUniqueConstraint && key != null) {
                    continue;
                }

                //如果参数指定了更新的字段且当前字段不包括在其中，则当前字段不会出现在update的set部分
                if (specifiedProp != null && !isContained(specifiedProp, pd.getName())) {
                    continue;
                }

                //设置update的set部分
                if (persist != null && !persist.name().trim().equals("")) {
                    setVar.append(persist.name()).append("=:").append(pd.getName()).append(",");
                } else {
                    setVar.append(toDBName(pd.getName())).append("=:").append(pd.getName()).append(",");
                }

            }

        } catch (NoSuchFieldException e) {
            throw new SqlCompileException("生成insert语句异常：" + e.getMessage(), e);
        }

        String setStr = setVar.toString().substring(0, setVar.length() - 1);
        sql.append(setStr).append(whereVar);

        SqlCompiler.UPDATE_SQL_C.put(beanClass.getName()+sps, sql.toString());

        return sql.toString();
    }


    /**
     * 指定的Bean是否保存唯一约束字段
     *
     * @param beanClass beanClass
     * @param pdArr     PropertyDescriptor[]
     * @return true: 类中包含唯一约束，false：类中不包含唯一约束
     */
    public static <T> boolean hasUniqueConstraint(Class<T> beanClass, PropertyDescriptor[] pdArr) {
        try {
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getName().equals("class")) continue;
                Field f = BeanUtil.getField(beanClass, pd.getName());
                if (f.getDeclaredAnnotation(Unique.class) != null) return true;
            }
        } catch (NoSuchFieldException e) {
            throw new SqlCompileException("生成insert语句异常：" + e.getMessage(), e);
        }
        return false;
    }

    public static <T> String getAutoIncrementColName(Class<T> beanClass) {

        PropertyDescriptor[] pdArr = BeanUtil.getPropertyDescriptors(beanClass);

        try {
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getName().equals("class")) continue;
                Field f = BeanUtil.getField(beanClass, pd.getName());
                Key key = f.getDeclaredAnnotation(Key.class);
                if (key != null && key.autoIncrement()) return pd.getName();
                Unique unique = f.getDeclaredAnnotation(Unique.class);
                if (unique != null && unique.autoIncrement()) return pd.getName();
            }
        } catch (NoSuchFieldException e) {
            throw new SqlCompileException("获取自增字段时发生异常：" + e.getMessage(), e);
        }

        return null;
    }

    private static Map<String,String> DEL_SQL_C = new ConcurrentHashMap<>();

    /**
     * 通过指定的Bean生成Delete语句.
     *
     * @param beanClass BeanClass
     * @return String
     * @throws SqlCompileException
     */
    public static <T> String getDelSql(Class<T> beanClass) throws SqlCompileException {

        if(SqlCompiler.DEL_SQL_C.containsKey(beanClass.getName())) {
            return SqlCompiler.DEL_SQL_C.get(beanClass.getName());
        }

        String tableName = getTableName(beanClass);
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM " + tableName);
        StringBuilder whereVar = new StringBuilder();

        PropertyDescriptor[] pdArr = BeanUtil.getPropertyDescriptors(beanClass);
        try {
            for (PropertyDescriptor pd : pdArr) {
                if (pd.getName().equals("class")) continue;
                Field f = beanClass.getDeclaredField(pd.getName());
                Key key = f.getDeclaredAnnotation(Key.class);
                Persist persist = f.getDeclaredAnnotation(Persist.class);
                Unique unique = f.getDeclaredAnnotation(Unique.class);

                boolean hasUniqueConstraint = hasUniqueConstraint(beanClass, pdArr);

                /*
                 * 设置delete语句的Where部分，如果Bean中指定了唯一约束且当前的属性指定了唯一约束在优先考虑使用唯一约束，
                 * 否则使用指定为Key的属性生成
                 */
                if ((hasUniqueConstraint && unique != null) || (!hasUniqueConstraint && key != null)) {
                    String collName;
                    if (persist != null && !persist.name().trim().equals("")) {
                        collName = persist.name();
                    } else {
                        collName = toDBName(pd.getName());
                    }

                    if (whereVar.length() == 0) {
                        whereVar.append(" WHERE ").append(collName).append("=:").append(pd.getName());
                    } else {
                        whereVar.append(" AND ").append(collName).append("=:").append(pd.getName());
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            throw new SqlCompileException("生成Delete语句异常：" + e.getMessage(), e);
        }

        if (whereVar.length() <= 0) throw new SqlCompileException("生成Delete语句异常：Bean中没有字段指定为key");
        sql.append(whereVar);

        SqlCompiler.DEL_SQL_C.put(beanClass.getName(), sql.toString());

        return sql.toString();
    }


    public static boolean isContained(String[] specifiedProp, String propName) {
        for (String p : specifiedProp) {
            if (p.equals(propName)) return true;
        }
        return false;
    }


    /**
     * 将类名或驼峰风格的字段名按默认规则转换成表名或表中的字段名。
     * <p>
     * 当类没有使用<Code>@Table</Code>注解指定表名，或字段
     * 没有用<code>@Persist</code>注解指定表中字段名时，会
     * 使用该方法执行转换。
     * </p>
     *
     * @param propName
     * @return
     */
    static String toDBName(String propName) {

        StringBuilder dbName = new StringBuilder();
        char[] charArray = propName.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (c >= 97 && c <= 122) dbName.append((char) (c - 32));
                else dbName.append(c);
            } else if (i > 0 && (c <= 90 && c >= 65)) {
                dbName.append("_").append(c);
            } else {
                if (c >= 97 && c <= 122)
                    dbName.append((char) (c - 32));
                else
                    dbName.append(c);
            }
        }

        return dbName.toString();
    }


    /**
     * 将数据表中的字段名翻译为Bean中的属性名，按驼峰风格
     *
     * @param dbFieldName
     * @return
     */
    public static String toHumpPropName(String dbFieldName) {
        StringBuilder propName = new StringBuilder();
        char[] charArray = dbFieldName.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (c >= 65 && c <= 90) propName.append((char) (c + 32));
                else propName.append(c);
            } else if (c == 95) {
                char t = charArray[i + 1];
                if (t >= 97 && t <= 122) propName.append((char) (t - 32));
                else propName.append(t);
                i++;
            } else {
                if (c >= 65 && c <= 90) propName.append((char) (c + 32));
                else propName.append(c);
            }
        }

        return propName.toString();
    }

    private static Dialect dialect;

    /*
     * 获取数据库方言
     * @return Dialect
     */
    public static Dialect getDialect() {

        if (dialect != null) return dialect;
        return dialect = MysqlDialect.getInstance();
    }

}
