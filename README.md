# dynamic-schedule
## quartz.properties
`org.quartz.scheduler.instanceName=springboot-quartz`   

`org.quartz.scheduler.instanceId=AUTO 또는 SYS_PROP`   
모든 문자열이 될 수 있지만 클러스터 내에서 동일한 '논리적' 스케줄러인 것처럼 작동하는 모든 스케줄러에 대해 고유해야 합니다.    
Id가 생성되도록 하려면 "AUTO" 값을 instanceId로 사용할 수 있습니다.    
또는 시스템 속성 "org.quartz.scheduler.instanceId"에서 값을 가져오려는 경우 "SYS_PROP" 값   

`org.quartz.threadPool.threadCount`   
내부 쓰레드 갯수를 지정합니다.

`org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX`     
JDBC-JobStoreTX 구성(JDBC를 통해 데이터베이스에 작업 및 트리거 저장)   
JDBCJobStore는 관계형 데이터베이스 내에 일정 정보(작업, 트리거 및 달력)를 저장하는 데 사용됩니다.    
실제로 필요한 트랜잭션 동작에 따라 선택할 수 있는 두 개의 개별 JDBCJobStore 클래스가 있습니다.    
JobStoreTX는 모든 작업(예: 작업 추가) 후에 데이터베이스 연결에서 commit()(또는 rollback())을 호출하여 모든 트랜잭션 자체를 관리합니다.    
JDBCJobStore는 독립 실행형 애플리케이션에서 Quartz를 사용하는 경우에 적합하고 애플리케이션이 JTA 트랜잭션을 사용하지 않는 경우 서블릿 컨테이너 내에서 사용하는 경우에 적합합니다.    

`org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate`
- org.quartz.impl.jdbcjobstore.StdJDBCDelegate (for fully JDBC-compliant drivers)
- org.quartz.impl.jdbcjobstore.MSSQLDelegate (for Microsoft SQL Server, and Sybase)
- org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
- org.quartz.impl.jdbcjobstore.WebLogicDelegate (for WebLogic drivers)
- org.quartz.impl.jdbcjobstore.oracle.OracleDelegate
- org.quartz.impl.jdbcjobstore.oracle.WebLogicOracleDelegate (for Oracle drivers used within Weblogic)
- org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate (for Oracle drivers used within Weblogic)

`org.quartz.jobStore.misfireThreshold=60000`     
스케줄러가 "실패"로 간주되기 전에 다음 실행 시간을 통과하기 위해 트리거를 '허용'하는 시간(밀리초)입니다.   
기본값(구성에서 이 속성을 입력하지 않은 경우)은 60000(60초)입니다.   

`org.quartz.jobStore.tablePrefix=QRTZ_`   
 데이터베이스에서 생성된 Quartz의 테이블에 주어진 접두사와 동일한 문자열입니다.    
 서로 다른 테이블 접두사를 사용하는 경우 동일한 데이터베이스 내에 여러 개의 Quartz 테이블 세트를 가질 수 있습니다.   
 
`org.quartz.jobStore.isClustered=true`      
클러스터링 기능을 켜려면 "true"로 설정하십시오.    
Quartz의 여러 인스턴스가 동일한 데이터베이스 테이블 세트를 사용하도록 하는 경우 이 속성을 "true"로 설정해야 합니다.   

`org.quartz.jobStore.clusterCheckinInterval=20000`    
이 인스턴스가 클러스터의 다른 인스턴스에 "체크인"*하는 빈도(밀리초)를 설정합니다.    
실패한 인스턴스를 감지하는 속도에 영향을 줍니다.  

## Cron Expression
![](../resources/static/images/cronExpression.PNG)