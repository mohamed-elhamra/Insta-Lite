import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { News } from 'src/app/models/news';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {
  newsArray: News[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get('https://cors-anywhere.herokuapp.com/https://www.lemonde.fr/rss/une.xml', { responseType: 'text' })
      .subscribe( data => {
        const news = this.parseXML(data, 'lemonde');
        this.newsArray.push(news);
      });
    this.http.get('https://cors-anywhere.herokuapp.com/https://www.lefigaro.fr/rss/figaro_actualites.xml', { responseType: 'text' })
      .subscribe(data => {
        const news = this.parseXML(data, 'lefigaro');
        this.newsArray.push(news);
      });
    this.http.get('https://cors-anywhere.herokuapp.com/https://www.france24.com/fr/actualites/rss', { responseType: 'text' })
      .subscribe(data => {
        const news = this.parseXML(data, 'france24');
        this.newsArray.push(news);
      });
  }

  parseXML(data: any, journal: string): News {
    const parser = new DOMParser();
    const xml = parser.parseFromString(data, 'text/xml');
    const items = xml.querySelectorAll('item');
    var image;
    if(journal == 'lemonde') {
      image = 'https://www.lemonde.fr/favicon.ico';
    } else if(journal == 'lefigaro') {
      image = 'https://www.lefigaro.fr/favicon.ico';
    } else if(journal == 'france24') {
      image = 'https://www.france24.com/favicon.ico';
    } else {
      image = '';
    }

      const title = items[0].querySelector('title')?.innerHTML.replace('<![CDATA[', '').replace(']]>', '') || '';
      const abstract = items[0].querySelector('description')?.innerHTML.replace('<![CDATA[', '').replace(']]>', '') || '';
    return {title, abstract, image}
  }

}
