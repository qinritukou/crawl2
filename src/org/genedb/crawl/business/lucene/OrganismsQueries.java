package org.genedb.crawl.business.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.genedb.crawl.CrawlErrorType;
import org.genedb.crawl.CrawlException;
import org.genedb.crawl.model.MappedOrganism;
import org.genedb.crawl.model.MappedOrganismList;
import org.genedb.crawl.model.interfaces.Organisms;
import org.springframework.stereotype.Component;

@Component
public class OrganismsQueries extends Base implements Organisms {

	@Override
	public MappedOrganismList list()  throws CrawlException {
		
		IndexReader reader = repo.luceneIndexReader();
		IndexSearcher searcher = new IndexSearcher(reader);
		
		// prefix query with empty string, returns docs with all non empty values for organism.common_name
		PrefixQuery pq = new PrefixQuery(new Term("organism.common_name", "")); 
		
		MappedOrganismList organisms = new MappedOrganismList();
		
		try {
			TopDocs td = searcher.search(pq, reader.maxDoc());
			
			for (ScoreDoc sd : td.scoreDocs) {
				
				Document d = reader.document(sd.doc);
				
				MappedOrganism o = new MappedOrganism ();
				o.common_name = d.getField("organism.common_name").stringValue();
				o.genus = d.getField("organism.common_name").stringValue();
				o.species = d.getField("organism.common_name").stringValue();
				o.taxonID = d.getField("organism.common_name").stringValue();
				o.translation_table = d.getField("organism.common_name").stringValue();
				
				o.name = o.genus + " " + o.species;
				
				organisms.list.add(o);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new CrawlException("Could not execute query.", CrawlErrorType.QUERY_FAILURE);
		}
		
		// TODO Auto-generated method stub
		return organisms;
	}

}