#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <curl/curl.h>

int main(int argc, char** argv) {
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