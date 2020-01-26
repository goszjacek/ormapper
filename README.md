# ormapper

## Description file format:

### Example:

	<src-class name="Laptop" table="laptop" >
		<properties>
			<property name="laptopId" column="laptop_id" type="int" feature="id"></property>
			<property name="laptopCategoryId" column="laptop_category_id" type="int"></property>
			<property name="model" column="model" type="text"></property>
		</properties>
	</src-class>
	
### Possible Nodes:

#### src-class

Describes basic information about a source class mapped using framework.

Attributes:
- name - name of the class in Java
- table - name of corresponding table in Database

Nodes: 
- properties

#### Properties
Parent tag including properties.

Attributes:
empty

Nodes:
- property

#### Property

Describes one single field of the class.

Attributes:
- name
- column
- type - possible values: "int", "string". If feature is specified, type doesn't need to be specified, because always "int"
- feature - possible values: "id", "onetoone", "bionetoone"
Note: Attributes of Property Node are not case sensitive. 

  
