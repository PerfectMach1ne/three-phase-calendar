#define __STDC_WANT_LIB_EXT1__ 1

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include <curl/curl.h>
#include <ncurses.h>

void draw_monthdays() {
#ifdef __STDC_LIB_EXT1__
  time_t t = time(NULL);
  struct tm buf;
  char str[26];
  printf("asctime: %s", asctime_s(str, sizeof str, localtime_s(&t, &buf))); // asctime() appends a '\n' at the end of the string.
  printf("Today is %s", )
#else
  time_t t = time(NULL);
  printf("asctime: %s", asctime(localtime(&t))); // asctime() appends a '\n' at the end of the string.
#endif
}

void draw_weekdays() {
  char* weekdays[7] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  for (int i = 0; i < sizeof(weekdays) / sizeof(char*); i++) {
    if (i == 0) {
      printf("╔═════╦"); 
    } else if (i == 6) {
      printf("═════╗\n"); 
    } else {
      printf("═════╦"); 
    }
  }
  for (int j = 0; j < sizeof(weekdays) / sizeof(char*); j++) { 
    if (j == 0) {
      printf("║ %s ║", weekdays[j]); 
    } else if (j == 6) {
      printf(" %s ║\n", weekdays[j]); 
    } else {
      printf(" %s ║", weekdays[j]); 
    }
  }
  for (int k = 0; k < sizeof(weekdays) / sizeof(char*); k++) { 
    if (k == 0) {
      printf("╚═════╩"); 
    } else if (k == 6) {
      printf("═════╝\n"); 
    } else {
      printf("═════╩"); 
    }
  }
}

int main(int argc, char** argv) {
  draw_monthdays();
  draw_weekdays();
  printf("what\n");
  CURL* curl;
  CURLcode res;

  curl = curl_easy_init();
  
  if (curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://127.0.0.1:8057/testTask");
    /* Do not do the transfer - only connect to host */
    // curl_easy_setopt(curl, CURLOPT_CONNECT_ONLY, 1L);

    res = curl_easy_perform(curl);

    if (res != CURLE_OK) {
      fprintf(stderr, "curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
    } else {
      char buf[512];
      size_t nread;
      long sockfd;
      char *ct;
      res = curl_easy_getinfo(curl, CURLINFO_CONTENT_TYPE, &ct);
      
      if ((CURLE_OK == res) && ct) {
        printf("Wondrous! We have received Content-Type %s!\n", &ct);
      }

      res = curl_easy_getinfo(curl, CURLINFO_ACTIVESOCKET, &sockfd);

      res = curl_easy_recv(curl, buf, sizeof(buf), &nread);
      printf("%s", &buf);
    }

    curl_easy_cleanup(curl);
  }

  return EXIT_SUCCESS;
}