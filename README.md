# What is Apache Flink?

Apache Flink is an open-source stream processing framework that provides powerful capabilities for processing and analyzing large-scale data streams in real-time. It offers a unified system for both batch and stream processing, enabling users to build robust, scalable, and fault-tolerant data processing applications.

Use Homebrew to install the latest version of Apache Flink. We'll be using version 1.18.1.

`brew install apache-flink`

You can update `$PATH` in `~/.zshrc` (or `~/.zprofile`) and add `/usr/local/Cellar/apache-flink/1.18.1/bin` directory to make your life easier.

# Flink Concepts

**JobManager**: In a Flink cluster, there is typically one JobManager. It coordinates the execution of Flink jobs. In addition, it is responsible for accepting jobs from clients, scheduling tasks, and coordinating checkpoints and failure recovery.

**TaskManagers**: There are one or more TaskManagers in the cluster. TaskManagers execute the actual computation tasks. They run one or more parallel instances of the tasks assigned to them by the JobManager.

**Networking**: JobManager and TaskManagers communicate with each other via a messaging system. They exchange control messages, data streams, and coordinate distributed computations.

**State Management**: Flink manages the state of streaming applications, allowing them to maintain stateful operations efficiently. This state management can be distributed across TaskManagers and supports fault tolerance through checkpoints and state backups.

**Fault Tolerance**: Flink clusters are designed to be fault-tolerant. They support mechanisms such as checkpoints and state backups to recover from failures and ensure data consistency.

**Scalability**: Flink clusters can scale horizontally by adding or removing TaskManagers dynamically to handle varying workloads. This allows the system to adapt to changing resource requirements.

# Setup Local Standalone Flink Cluster

A local Flink cluster refers to a Flink cluster that runs entirely within a single machine, typically for development, testing, or small-scale processing tasks.

**JobManager and TaskManagers**: Both the JobManager and TaskManagers run within the same JVM process on the local machine. This means that there is no network communication overhead between them, as they are all running in the same process.

**Testing and Development**: Local Flink clusters are commonly used for testing and development of Flink applications. 

**Single Node Processing**: Since a local Flink cluster runs on a single machine, it is typically used for processing tasks that can be handled by a single node.

Overall, a local Flink cluster provides a convenient environment for developers to experiment, debug, and test Flink applications locally before deploying them to a production environment.

## Verify Setup

Let's make sure that Flink is setup on your local system. 

Start the local Flink cluster in Session mode:

```
>/usr/local/Cellar/apache-flink/1.18.1/libexec/bin/start-cluster.sh
Starting cluster.
Starting standalonesession daemon on host D01ZK199MSDV.
Starting taskexecutor daemon on host D01ZK199MSDV.
```

The cluster will be launched and you can access the Flink UI at `localhost:8081`.

<img width="927" alt="Flink UI running on localhost" src="https://github.com/pdesai5839/flink_stream/assets/143283961/738e55c9-20ae-4e1a-a5c4-9db9bae32843">

Stop the local Flink cluster:

```
/usr/local/Cellar/apache-flink/1.18.1/libexec/bin/stop-cluster.sh
Stopping taskexecutor daemon (pid: 27073) on host D01ZK199MSDV.
Stopping standalonesession daemon (pid: 26812) on host D01ZK199MSDV.
```
# Setup Flink Cluster Using Docker

Check the [official documentation](https://nightlies.apache.org/flink/flink-docs-release-1.18/docs/deployment/resource-providers/standalone/docker/#getting-started) on how to run Flink cluster using Docker containers.

The `docker-compose.yml` file in the project makes it easy to run a Flink cluster on Docker. It will launch Flink in Session mode and allow us to manually submit jobs to the JobManager.

Start the Flink cluster using:

`docker-compose up`

If all goes well, the Flink UI will be up at `localhost:8081`. 

Stop the Flink cluster using:

`docker-compose down`

## Submit & Run a Flink Job

Since our Flink cluster is running in Session mode, we can manually submit jobs to the **JobManager**. This can be accomplished in two ways: Command line and Web Interface running on `localhost:8081`.

### Command Line
Run the following command:

```
flink run --class <classname> <path_to_jar>
```

Now, we need to open a new terminal inside the JobManager container and run the command:

```
docker exec -it flink-jobmanager /bin/bash
flink run --class <classname> <path_to_jar>
```

### Web Interface
Alternatively, you can submit the job by uploading the jar file from the Flink UI running at `localhost:8081`.

# Build & Submit a Job

Checkout the repository and run:

```
mvn clean package
```

The command above will produce a Jar file in the `/target` directory.

Let's try to submit a simple `WordCountJob` which uses a couple of verses from the classic Run-DMC rap 'Rock Box'. This is obviously a low effort Job, however one can imagine a continuous stream of data being fed to Flink. 

```
flink run \
  --class com.desai.demo.flink.wordcount.WordCountJob \
  ~/git/flink_stream/target/flink_stream_demo-0.0.1-SNAPSHOT.jar
```

Alternatively, we can use the Flink UI to submit the job. A successful run will look like this:

<img width="1082" alt="Word Count Result" src="https://github.com/pdesai5839/flink_stream/assets/143283961/b129eb65-7a84-42c0-8937-c2d62d1548b5">
