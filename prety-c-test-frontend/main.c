#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

#include <curl/curl.h>
#include <ncurses.h>

void draw_datestring() {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[20];

  // Draw 1st box ceiling
  for (int i = 0; i < 43; i++) {
    if (i == 0) printf("╔");
    else if (i < 42 ) printf("═");
    else printf("╗\n");
  }

  // Draw 1st box content & walls
  printf("║");
  char* msg_buf = (char*)malloc(35*sizeof(char));
  char fstr[11] = "%e";
  // Prepare the English format string.
  if (lt->tm_mday < 10) {
    switch (lt->tm_mday % 10) {
      case 1:
        if (lt->tm_mday == 11) break;
        strcat(fstr, "st %B %Y");
        break;
      case 2:
        if (lt->tm_mday == 12) break;
        strcat(fstr, "nd %B %Y");
        break;
      case 3:
        if (lt->tm_mday == 13) break;
        strcat(fstr, "rd %B %Y");
        break;
      default:
        strcat(fstr, "th %B %Y");
    }
  }
  size_t spaces_to_fill = 0;
  if (strftime(tstr_buf, sizeof(tstr_buf), fstr, lt)) {
    // The point of this if statement is just to ensure there is no ugly extra space from strftime() on monthdays < 10
    if (lt->tm_mday < 10) {
      // Current width of the entire box = 43
      spaces_to_fill = 2 + 9 + strlen(tstr_buf);
      spaces_to_fill = 43 - spaces_to_fill;
      int left_space_count = (int)(spaces_to_fill / 2);
      int right_space_count = spaces_to_fill - left_space_count;
      char left_space[left_space_count + 1];
      char right_space[right_space_count + 1];

      for (int i = 0; i < left_space_count; i++) printf(" ");
      printf("Today is%s!", &tstr_buf);
      for (int i = 0; i < right_space_count; i++) printf(" ");
    } else {
      spaces_to_fill = 2 + 10 + strlen(tstr_buf);
      spaces_to_fill = 43 - spaces_to_fill;
      int left_space_count = (int)(spaces_to_fill / 2);
      int right_space_count = spaces_to_fill - left_space_count;
      char left_space[left_space_count + 1];
      char right_space[right_space_count + 1];
      printf("Today is %s!", &tstr_buf);
    }
  } else { puts("strftime failed"); }
  printf("║\n");
}

void draw_monthdays() {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[7];

  // Draw 1st box floor & 2nd box ceiling
  for (int i = 0; i < 7; i++) {
    if (i == 0) {
      printf("╠═════╦"); 
    } else if (i == 6) {
      printf("═════╣\n"); 
    } else {
      printf("═════╦"); 
    }
  }

  // Draw 2nd box content & walls
  printf("║");
  // For testing if it doesn't get wrecked on Sundays and monthdays > 7:
  // lt->tm_mday += 8 % 31; lt->tm_wday += 8 % 7;
  lt->tm_mday = (lt->tm_mday - lt->tm_wday % 7 + 1) % 31; // Potentially flawed on [28,30] monthday months.
  mktime(lt);
  for (int i = 0; i < 7; i++) {
    /* https://pubs.opengroup.org/onlinepubs/009695399/functions/mktime.html
     * "The original values of the tm_wday and tm_yday components of the structure are ignored, [...]"" 
     * ...can you guess what I tried to do before?
    */
    lt->tm_mday = (i != 0 ? lt->tm_mday + 1 : lt->tm_mday) % 31; // Potentially flawed on [28,30] monthday months.

    time_t newt = mktime(lt);
    lt = localtime(&newt);

    if (strftime(tstr_buf, sizeof(tstr_buf), "%d.%m", lt)) {
      printf("%s║", &tstr_buf);
    } else { puts("strftime failed"); }
  }
  printf("\n");
}

void draw_weekdays() {
  char* weekdays[7] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

  // Draw 2nd box floor & 3rd box ceiling
  for (int i = 0; i < sizeof(weekdays) / sizeof(char*); i++) {
    if (i == 0) {
      printf("╠═════╬"); 
    } else if (i == 6) {
      printf("═════╣\n"); 
    } else {
      printf("═════╬"); 
    }
  }

  // Draw 3rd box content & walls
  for (int j = 0; j < sizeof(weekdays) / sizeof(char*); j++) { 
    if (j == 0) {
      printf("║ %s ║", weekdays[j]); 
    } else if (j == 6) {
      printf(" %s ║\n", weekdays[j]); 
    } else {
      printf(" %s ║", weekdays[j]); 
    }
  }

  // Draw 3rd box floor
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
  CURL* curl;
  CURLcode res;

  curl = curl_easy_init();

  draw_datestring();
  draw_monthdays();
  draw_weekdays();
  
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
      
      // this doesn't work lol
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