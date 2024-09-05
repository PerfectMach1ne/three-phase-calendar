#define _XOPEN_SOURCE_EXTENDED 700

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <locale.h>
#include <time.h>

#include <curl/curl.h>
#include <ncurses.h>

#define SMALL_Q 113

typedef struct _win_border_struct {
  chtype ls, rs, ts, bs,
         tl, tr, bl, br;
} WIN_BORDER;

typedef struct _WIN_struct {
  int startx, starty;
  int height, width;
  WIN_BORDER border;
} WIN;

WINDOW* create_newwin(int height, int width, int starty, int startx);
void destroy_win(WINDOW* local_win);
// void draw_datestring_W(WINDOW* local_win);
// void draw_monthdays_W(WINDOW* local_win);
// void draw_weekdays_W(WINDOW* local_win);
// void draw_hours_W(WINDOW* local_win);

int main(int argc, char** argv) {
  WINDOW* filler_W;
  // WINDOW* filler_W;
  WINDOW* datestring_W;
  WINDOW* monthdays_W;
  WINDOW* weekdays_W;
  WINDOW* hours_W;
  int height, width, starty, startx;
  int ch;
  int ch_mult = 0;

  CURL* curl;
  CURLcode res;

  curl = curl_easy_init();

  setlocale(LC_ALL, "");
  
  initscr();
  cbreak(); // Disable line buffering
  noecho();
  keypad(stdscr, TRUE);

  height = 5; width = 7;
  starty = 1; startx = 1;
  // test_box = create_newwin(height, width, starty, startx);
  // wborder(test_box, '|', '|', '-', '-', '+', '+', '+', '+');
  filler_W = create_newwin(7, 9, 0, 0);
  datestring_W = create_newwin(3, 42, 0, 9);
  monthdays_W = create_newwin(3, 42, 2, 9);

  while ( (ch = getch()) != SMALL_Q ) {
    if ( filler_W != NULL ) destroy_win(filler_W);
    if ( datestring_W != NULL ) destroy_win(datestring_W);
    if ( monthdays_W != NULL ) destroy_win(monthdays_W);
    
    filler_W = create_newwin(7, 9, 0, 0);
    datestring_W = create_newwin(3, 42, 0, 8);
    monthdays_W = create_newwin(3, 42, 2, 8);
    cchar_t ls; setcchar(&ls, L"║", 0, 0, NULL);
    cchar_t rs; setcchar(&rs, L"║", 0, 0, NULL);
    cchar_t ts; setcchar(&ts, L"═", 0, 0, NULL);
    cchar_t bs; setcchar(&bs, L"═", 0, 0, NULL);
    cchar_t tl; setcchar(&tl, L"╔", 0, 0, NULL);
    cchar_t tr; setcchar(&tr, L"╗", 0, 0, NULL);
    cchar_t bl; setcchar(&bl, L"╚", 0, 0, NULL);
    cchar_t br; setcchar(&br, L"╝", 0, 0, NULL);

    wborder_set(filler_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(filler_W);
    // printw("");
    
    // wrefresh(test_box);

    // draw_datestring();
    // draw_monthdays();
    // draw_weekdays();
  }
  
  destroy_win(filler_W);
  destroy_win(datestring_W);
  // if (curl) {
  //   curl_easy_setopt(curl, CURLOPT_URL, "http://127.0.0.1:8057/testTask");
  //   /* Do not do the transfer - only connect to host */
  //   // curl_easy_setopt(curl, CURLOPT_CONNECT_ONLY, 1L);

  //   res = curl_easy_perform(curl);

  //   if (res != CURLE_OK) {
  //     fprintf(stderr, "curl_easy_perform() failed: %s\n",
  //                   curl_easy_strerror(res));
  //   } else {
  //     char buf[512];
  //     size_t nread;
  //     long sockfd;
  //     char *ct;
  //     res = curl_easy_getinfo(curl, CURLINFO_CONTENT_TYPE, &ct);
      
  //     // this doesn't work lol
  //     if ((CURLE_OK == res) && ct) {
  //       printf("Wondrous! We have received Content-Type %s!\n", &ct);
  //     }

  //     res = curl_easy_getinfo(curl, CURLINFO_ACTIVESOCKET, &sockfd);

  //     res = curl_easy_recv(curl, buf, sizeof(buf), &nread);
  //     printf("%s", &buf);
  //   }

  //   curl_easy_cleanup(curl);
  // }
  refresh();

  endwin();
  return EXIT_SUCCESS;
}

WINDOW* create_newwin(int height, int width, int starty, int startx) {
  WINDOW* local_win;
  
  local_win = newwin(height, width, starty, startx);
  box(local_win, 0, 0);
  // wborder(local_win, '|', '|', '-', '-', '+', '+', '+', '+');
  wrefresh(local_win); // Refresh the window to show the bocx

  return local_win;
}

void destroy_win(WINDOW* local_win) {
  wborder(local_win, ' ', ' ', ' ',' ',' ',' ',' ',' ');
  wrefresh(local_win);
  delwin(local_win);
}

void draw_datestring() {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[20];

  // Draw 1st box ceiling
  for (int i = 0; i < 43; i++) {
    if (i == 0) printf("╦");
    else if (i < 42 ) printf("═");
    else printf("╗\n");
  }

  // Draw 1st box content & walls
  
  // Prepare the English format string.
  char fstr[11] = "%e";
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
  
  printf("║");
  if (strftime(tstr_buf, sizeof(tstr_buf), fstr, lt)) {
    size_t spaces_to_fill = 0;
    
    spaces_to_fill = 2 + (lt->tm_mday < 10 ? 9 : 10) + strlen(tstr_buf);
    spaces_to_fill = 43 - spaces_to_fill; // Current width of the entire box = 43

    int left_space_count = (int)(spaces_to_fill / 2);
    int right_space_count = spaces_to_fill - left_space_count;

    char left_space[left_space_count + 1];
    char right_space[right_space_count + 1];

    for (int i = 0; i < left_space_count; i++) printf(" ");
    printf("Today is%s!", &tstr_buf);
    for (int i = 0; i < right_space_count; i++) printf(" ");
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
      printw("╠═════╬"); 
    } else if (i == 6) {
      printw("═════╣\n"); 
    } else {
      printw("═════╬"); 
    }
  }

  // Draw 3rd box content & walls
  for (int j = 0; j < sizeof(weekdays) / sizeof(char*); j++) { 
    if (j == 0) {
      printw("║ %s ║", weekdays[j]); 
    } else if (j == 6) {
      printw(" %s ║\n", weekdays[j]); 
    } else {
      printw(" %s ║", weekdays[j]); 
    }
  }

  // Draw 3rd box floor
  for (int k = 0; k < sizeof(weekdays) / sizeof(char*); k++) { 
    if (k == 0) {
      printw("╚═════╩"); 
    } else if (k == 6) {
      printw("═════╝\n"); 
    } else {
      printw("═════╩"); 
    }
  }
  // refresh();
}
