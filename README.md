# Projeto de Análise dos Logs de Acesso

| Branch | Build |
|:---:|:---:|
| Master | [![Build Status](https://travis-ci.com/BrunoVieira319/log-access-analytics.svg?branch=master)](https://travis-ci.com/BrunoVieira319/log-access-analytics) |

## Introdução
Este projeto é uma API Rest que recebe logs em texto, salva em um banco de dados e expõe métricas destes logs.

## Principais tecnologias/recursos utilizados
* [Java 8](https://www.java.com/pt_BR/download/faq/java8.xml) - Linguagem de programação utilizada no projeto
* [Gradle](https://gradle.org) - Gerenciador de dependências
* [Dropwizard](https://www.dropwizard.io/1.3.12/docs) - Framework de Java para RESTful Web Services
* [JUnit](https://junit.org/junit4) - Framework de testes para Java
* [Mockito](https://site.mockito.org) - Framework que ajuda a compor e isolar testes
* [MongoDB](https://www.mongodb.com) - Banco de dados NoSQL orientado a documentos
* [Flapdoodle](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) - Banco de dados MongoDB em memória para testes
* [Flogger](https://google.github.io/flogger) - API de logs da Google
* [Jenkins](https://jenkins.io) - Ferramenta para automação de processos
* [Terraform](https://www.terraform.io) - Ferramenta multi-plataforma para construção de infraestrutura
* [Packer](https://www.packer.io) - Ferramenta multi-plataforma para construção de imagens
* [Ansible](https://www.ansible.com) - Ferramenta de provisionamento de software
* [Google Cloud Platform](https://cloud.google.com/?hl=pt-br) - Serviço de computação em nuvem da Google

## Funcionamento do projeto
O sistema recebe logs da seguinte maneira:
```
<URL> <Timestamp> <UUID> <Região>
```
Exemplo:
```
/pets/exotic/cats/10  1037825323957 5b019db5-b3d0-46d2-9963-437860af707f 1
/pets/guaipeca/dogs/1 1037825323957 5b019db5-b3d0-46d2-9963-437860af707g 2
/tiggers/bid/now      1037825323957 5b019db5-b3d0-46d2-9963-437860af707e 3
```
Salva esses logs no banco de dados e expõe métricas.

#### Endpoints: 
```
<POST>   :8082/laar/ingest
```
Este endpoint recebe os logs para serem salvos, é necessário informar no header da requisição o tipo do conteúdo que é text/plain.
```
<GET>   :8080/laa/metrics?day=dd-mm-yyyy&week=ww-yyyy&year=yyyy
```
Este endpoint expõe as seguintes métricas:
1. Top 3 URLs mais acessadas globalmente
2. Top 3 URLs mais acessadas por região
3. A URL menos acessada globalmente
4. Top 3 URLs mais acessadas em um determinado dia, semana e ano (devem ser fornecidos por query parâmetro como mostrado acima)
5. O minuto do dia com mais acessos

Você também pode acessar cada métrica separadamente
```
<GET>   :8080/laa/metrics/1
<GET>   :8080/laa/metrics/2
<GET>   :8080/laa/metrics/3
<GET>   :8080/laa/metrics/4?day=dd-mm-yyyy&week=ww-yyyy&year=yyyy
<GET>   :8080/laa/metrics/5
```
```
<GET>   :8084/laaa/health
```
Este endpoint retorna código 200 quando o sistema está ok e 500 em caso de alguma falha interna.

## Instalação do projeto localmente
#### Pré-requisitos
* Java 8 instalado.
* MongoDB instalado.
* Git instalado.
#### Instruções
* Clone o projeto para a sua máquina
```
git clone https://github.com/BrunoVieira319/log-access-analytics.git
```
* Entre no diretório e execute o script de inicialização do projeto
```
cd log-access-analytics
sh start.sh
```

#### Rodando os testes
Para rodar os testes execute o comando
```
./gradlew test
```
## Instalação do projeto em cloud com Jenkins, e demais ferramentas..
#### Pré-requisitos
* Jenkins rodando
* Git instalado
* Terraform instalado
* Packer instalado
* Ansible instalado
* Conta na Google Cloud Platform (Os recursos usados __não sao free tier__)

#### Instruções
**_Na Google Cloud Platform_**
* No console da Google Cloud Platform, comece criando um novo projeto clicando na aba ao lado direito do título _Google Cloud Platform_ no topo da página.
* Com o projeto criado você precisará do arquivo .json contendo as credenciais para o seu projeto. No menu lateral vá em _API e serviços > Credenciais_; em seguida _Criar credenciais > Chave da conta de serviço_; e crie uma Nova conta de serviços com o papel Proprietário.
* E por útlimo você precisará de um Bucket para armazenar o arquivo .tfstate gerado pelo Terraform. No menu lateral vá em _Storage > Navegador_ e crie um novo Intervalo com o nome __tf-state-laaa__

**_No Jenkins_**
* Inicialmente, você precisará de duas variáveis de ambiente. No menu lateral do Jenkins vá em _Manage Jenkins > Configure System_, marque a caixa Enviroment variables e coloque as seguintes variáveis: 

| Name | Value |
|:---:|:---:|
| GOOGLE_APPLICATION_CREDENTIALS | o caminho para o arquivo .json com as credenciais |
| TF_VAR_project_id | o id do projeto (pode ser encontrado na página inicial do projeto)|

* Crie os jobs no Jenkins para cada um dos seguintes repositórios

|  Nome do Job | Repositório |
| :---: | :---: |
| Log Access Analytics | https://github.com/BrunoVieira319/log-access-analytics.git |
| Packer | https://github.com/BrunoVieira319/packer-laa.git |
| Terraform | https://github.com/BrunoVieira319/terraform-laa.git |

* Para criar um job no Jenkins, no menu lateral vá em _New Item_, coloque o nome do job, marque a opção Pipeline e clique em _Ok_. Na guia _Pipeline_, em _Definition_ escolha a opção _Pipeline script from SCM_, em _SCM_ escolha _Git_, cole o link do repositório no campo _Repository URL_ e salve.
* Execute os Jobs. Clique no nome do job e em _Build Now_ no menu lateral.
* O Job Log Access Analytics verificará a integridade do projeto, se ele está compilando e os testes estão passando.
* O Job Packer criará a imagem na GCP com o projeto pronto pra ser executado.
* O Job Terraform criará uma instância com a imagem criada pelo Packer.
* Se tudo der certo, a instância estará rodando, e você a verá no console da Google Cloud Platform. Vá em _Compute Engine > Instância de VMs_ e entre nela por SSH clicando em _SSH_. 
* Vá para a pasta onde está o projeto e execute o script de inicialização como root
```
cd ../packer/project/
sudo sh start.sh
```
_Nota: A instância criada é do tipo n1-standard-4, pois pra rodar em paralelo os 3 sub-projetos deste projeto é necessário 3 núcleos._
* Assim que o projeto estiver rodando, ele estará disponível para o mundo e você poderá acessá-lo através do IP externo gerado.
```
http://<IP externo>:8084/laaa/health
```
_Interrompa a instância assim que acabar de testar para não ser taxado_

### Considerações Finais
* Em questão de DevOps, eu realmente gostaria de ter aprendido e usado outras coisas mais como Logs Centralizados, Service Discovery, Api Gateway, Stress Testing, mas não foi possível.
* Sobre as ferramentas DevOps, tentei fazer simples e funcional para que pelo menos o principal do projeto fosse entregue. Então não tenho dúvida que muitas questões de configurações e boas práticas ficaram de fora, até por que eu não domino as ferramentas.
* Os logs são salvos nos diretórios raíz de cada sub-projeto em arquivos xml que é o padrão do Java para logs.
* Ocasionalmente, um dos testes que usa o Flapdoodle falha, acredito que seja algum problema de concorrência pois ele demora pra iniciar, porém não consegui descobrir exatamente a causa.

### Possíveis melhorias
* Inicializar o projeto assim que a instância for criada, sem assim precisar fazer SSH.
* Agents nas pipelines do Jenkins usando Docker assim não precisando instalar várias das ferramentas DevOps.
* Jenkins rodando em uma instância na nuvem ao invés de localmente.
* Implementar alguma maneira de executar um terraform destroy.

### Autor
* Bruno da Graça Vieira
