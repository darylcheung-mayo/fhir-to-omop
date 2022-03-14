
truncate table cdm_source;

insert into cdm_source (
   cdm_source_name, 
   cdm_source_abbreviation,
   cdm_holder,
   source_description,
   source_documentation_reference,
   cdm_etl_reference,
   source_release_date,
   cdm_release_date,
   cdm_version,
   vocabulary_version,
   cdm_version_concept_id
) values (
   '@cdm_source_name',
   '@cdm_source_abbreviation',
   '@cdm_holder',
   '@source_documentation_reference',
   '@source_documentation_reference',
   '@cdm_etl_reference', 
   '@source_release_date',
   '@cdm_release_date',
   '@cdm_version',
   '@vocabulary_version',
   @cdm_version_concept_id
);

