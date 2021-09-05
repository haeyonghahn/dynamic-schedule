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
