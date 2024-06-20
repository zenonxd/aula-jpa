# Introdução JPA e Hibernate (bônus Maven e MySQL) 

Assista o vídeo desta aula:

[![Image](https://img.youtube.com/vi/CAP1IPgeJkw/mqdefault.jpg "Vídeo no Youtube")](https://youtu.be/CAP1IPgeJkw)

## Sumário
- [O que você vai aprender](#O-que-você-vai-aprender)
- [Pré-requisitos](#Pré-requisitos)
- [Visão geral sobre mapeamento objeto-relacional](#Visão-geral-sobre-mapeamento-objeto-relacional)
- [JPA](#JPA)
- [Criando uma aplicação simples](#Criando-uma-aplicação-simples)

## Conteúdo
- Visão geral sobre mapeamento objeto-relacional
- Introdução ao JPA - Java Persistence API

## Visão geral sobre mapeamento objeto-relacional

![myImage](https://github.com/devsuperior/aulao006/raw/master/img-problema-orm.png)

### Outros problemas que devem ser tratados:
- Contexto de persistência (objetos que estão ou não atrelados a uma conexão em um dado momento)
- Mapa de identidade (cache de objetos já carregados)
- Carregamento tardio (lazy loading)
- Outros

## JPA

Java Persistence API (JPA) é a especificação padrão da plataforma Java EE (pacote javax.persistence) para mapeamento objeto-relacional e persistência de dados.

JPA é apenas uma especificação (JSR 338):
http://download.oracle.com/otn-pub/jcp/persistence-2_1-fr-eval-spec/JavaPersistence.pdf

Para trabalhar com JPA é preciso incluir no projeto uma implementação da API (ex: Hibernate).

Arquitetura de uma aplicação que utiliza JPA:

![myImage](https://github.com/devsuperior/aulao006/raw/master/img-arquitetura-jpa.png)

### Principais classes:

#### EntityManager
https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html

Um objeto EntityManager encapsula uma conexão com a base de dados e serve para efetuar operações de acesso a dados (inserção, remoção, deleção, atualização) em entidades (clientes, produtos, pedidos, etc.) por ele monitoradas em um mesmo contexto de persistência.

Escopo: tipicamente mantem-se uma instância única de EntityManager para cada thread do sistema (no caso de aplicações web, para cada requisição ao sistema). 

#### EntityManagerFactory
https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManagerFactory.html

Um objeto EntityManagerFactory é utilizado para instanciar objetos EntityManager.

Escopo: tipicamente mantem-se uma instância única de EntityManagerFactory para toda aplicação.

## Criando uma aplicação simples

![myImage](https://github.com/devsuperior/aulao006/raw/master/img-pessoa.png)

### Passos

#### Crie uma base de dados MySQL vazia
- Instale o Xampp no seu computador
- Inicie o Apache e o MySQL
- No PhpMyAdmin, crie uma base de dados chamada "aulajpa"

#### Crie um novo projeto Maven
- File -> New -> Other -> Maven Project
- Create Simple Project -> Next
  - Group Id: com.educandoweb
  - Artifact Id: aulajpamaven
  -Finish

#### Copie as classes Programa e Pessoa para o novo projeto

```java
package dominio;

import java.io.Serializable;

public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String email;

	public Pessoa() {
	}

	public Pessoa(Integer id, String nome, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", email=" + email + "]";
	}
}
```

```java
package aplicacao;

import dominio.Pessoa;

public class Programa {

	public static void main(String[] args) {
		Pessoa p1 = new Pessoa(1, "Carlos da Silva", "carlos@gmail.com");
		Pessoa p2 = new Pessoa(2, "Joaquim Torres", "joaquim@gmail.com");
		Pessoa p3 = new Pessoa(3, "Ana Maria", "ana@gmail.com");

		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
	}
}
```

#### Atualize o Maven do projeto para Java 11
- Edite o arquivo pom.xml
- Inclua o conteúdo abaixo
- Salve o projeto
- Botão direito no projeto -> Maven -> Update Project

```xml
<properties>
	<maven.compiler.source>11</maven.compiler.source>
	<maven.compiler.target>11</maven.compiler.target>
</properties>
```

#### Inclua as dependências Maven a serem baixadas:
#### Não esquecer de aqui usar jakarta ao invés de javax.

```xml
    <dependencies>
  <!-- https://mvnrepository.com/artifact/org.hibernate/hibernate-core-jakarta -->
  <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core-jakarta</artifactId>
    <version>5.6.15.Final</version>
  </dependency>

  <!-- https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core -->
  <dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.5.2.Final</version>
  </dependency>


  <!-- https://mvnrepository.com/artifact/com.mysql/mysql-connector-j -->
  <dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
  </dependency>
</dependencies>
```

#### Configure o JPA no seu projeto por meio do arquivo persistence.xml
- Crie uma pasta "META-INF" a partir da pasta "resources"
- Dentro da pasta META-INF crie um arquivo "persistence.xml"
- Conteúdo do arquivo persistence.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
    http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd"
	version="2.1">

	<persistence-unit name="exemplo-jpa" transaction-type="RESOURCE_LOCAL">
	<properties>
		<property name="javax.persistence.jdbc.url"
			value="jdbc:mysql://localhost/aulajpa?useSSL=false&amp;serverTimezone=UTC" />

		<property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
		<property name="javax.persistence.jdbc.user" value="root" />
		<property name="javax.persistence.jdbc.password" value="" />

		<property name="hibernate.hbm2ddl.auto" value="update" />

		<!-- https://docs.jboss.org/hibernate/orm/5.4/javadocs/org/hibernate/dialect/package-summary.html -->
		<property name="hibernate.dialect" 	value="org.hibernate.dialect.MySQL8Dialect" />
	</properties>
	</persistence-unit>
</persistence>
```

#### Inclua os MAPEAMENTOS na classe de domínio:

```java
package dominio;

import (...)

@Entity
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	(...)
```

#### Na classe "Programa" faça os testes (veja vídeo-aula).

# COMANDOS

#### INICIANDO EntityManagerFactory

```java
    //passa a persistence name do xml
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
```

#### INICIANDO CONEXÃO COM BANCO DE DADOS
```java
    EntityManager em = emf.createEntityManager();
```

#### SALVANDO OBJETOS NO BANCO DE DADOS

```java
  em.persist(objeto);
```
#### BUSCANDO OBJETO PELO ID
<p>Instancia um novo objeto, definina o nome e passa o come find com o objeto.class e sua ID.</p> 

```java
Pessoa pFind = em.find(Pessoa.class, 2);
System.out.println(pFind);
```

#### REMOVENDO NO BANCO DE DADOS
<p>
  O JPA só consegue remover entidades que esteja monitorada. <br>
  Ou seja, ou é um objeto recém inserido.
  Ou um objeto que você precise buscar no banco de dados e ainda não fechou no EntityManager.
</p>

```java
    // Não esquecer de iniciar e commitar uma transação. Sempre que for uma operação sem ser consulta
    // devemos iniciar uma transação e finalizar ela commitando.
    em.getTransaction().begin();

    // Buscando entidade no banco de dados
    Pessoa p = em.find(Pessoa.class, 2);
    
    //Removendo
    em.remove(p);
    
    em.getTransaction().commit();

```


