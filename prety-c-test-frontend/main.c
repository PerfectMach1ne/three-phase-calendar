#define _XOPEN_SOURCE_EXTENDED 700 // An attempt at getting ncursesw to work; unnecessary thanks to cchar_t.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <locale.h>
#include <time.h>

#include <curl/curl.h>
#include <ncurses.h>

#define SMALL_Q 113

typedef struct _WIN_params {
  int height, width;
  int starty, startx;
} WIN_p;

typedef struct _WIN_border_params {
  cchar_t ls, rs, ts, bs,
         tl, tr, bl, br;
} WIN_bp;

WINDOW* create_newwin(int height, int width, int starty, int startx);
void destroy_win(WINDOW* local_win);
void draw_datestring_W(WINDOW* local_win);
void draw_monthdays_W(WINDOW* local_win);
void draw_weekdays_W(WINDOW* local_win);
void draw_hours_W(WINDOW* local_win);

int main(int argc, char** argv) {
  WINDOW* filler_W; WIN_p f_p = { 7, 9, 0, 0 };
  WINDOW* datestring_W; WIN_p ds_p = { 3, 42, 0, 8 };
  WINDOW* monthdays_W; WIN_p md_p = { 3, 42, 2, 8 };
  WINDOW* weekdays_W; WIN_p wd_p = { 3, 42, 4, 8 };
  WINDOW* hours_W; WIN_p hp_p;
  int ch;
  // int ch_mult = 0;

  CURL* curl;
  CURLcode res;

  curl = curl_easy_init();

  setlocale(LC_ALL, "");
  
  initscr();
  cbreak(); // Disable line buffering
  noecho();
  keypad(stdscr, TRUE);

  filler_W = create_newwin(f_p.height, f_p.width, f_p.starty, f_p.startx);
  datestring_W = create_newwin(ds_p.height, ds_p.width, ds_p.starty, ds_p.startx);
  monthdays_W = create_newwin(md_p.height, md_p.width, md_p.starty, md_p.startx);
  weekdays_W = create_newwin(wd_p.height, wd_p.width, wd_p.starty, wd_p.startx);

  while ( (ch = getch()) != SMALL_Q ) {
    if ( filler_W != NULL ) destroy_win(filler_W);
    if ( datestring_W != NULL ) destroy_win(datestring_W);
    if ( monthdays_W != NULL ) destroy_win(monthdays_W);
    if ( weekdays_W != NULL ) destroy_win(weekdays_W);
    
    filler_W = create_newwin(f_p.height, f_p.width, f_p.starty, f_p.startx);
    datestring_W = create_newwin(ds_p.height, ds_p.width, ds_p.starty, ds_p.startx);
    monthdays_W = create_newwin(md_p.height, md_p.width, md_p.starty, md_p.startx);
    weekdays_W = create_newwin(wd_p.height, wd_p.width, wd_p.starty, wd_p.startx);

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
    setcchar(&tl, L"╦", 0, 0, NULL);
    wborder_set(datestring_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(datestring_W);
    setcchar(&tl, L"╠", 0, 0, NULL);
    setcchar(&tr, L"╣", 0, 0, NULL);
    wborder_set(monthdays_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(monthdays_W);
    setcchar(&bl, L"╩", 0, 0, NULL);
    wborder_set(weekdays_W, &ls, &rs, &ts, &bs, &tl, &tr, &bl, &br);
    wrefresh(weekdays_W);
    // draw_datestring();
    // draw_monthdays();
    // draw_weekdays();
  }
  
  // Clean up windows after the loop breaks
  destroy_win(filler_W);
  destroy_win(datestring_W);
  destroy_win(monthdays_W);
  destroy_win(weekdays_W);
  if (curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://127.0.0.1:8057/teapot");
    /* Do not do the transfer - only connect to host */
    // curl_easy_setopt(curl, CURLOPT_CONNECT_ONLY, 1L);

    res = curl_easy_perform(curl);

    if (res != CURLE_OK) {
      printw("curl_easy_perform() failed: %s\n",
                    curl_easy_strerror(res));
    } else {
      char buf[512];
      size_t nread;
      long sockfd;
      char *ct;
      res = curl_easy_getinfo(curl, CURLINFO_CONTENT_TYPE, &ct);
      
      // this doesn't work lol
      if ((CURLE_OK == res) && ct) {
        printw("Wondrous! We have received Content-Type %s!\n", &ct);
      }

      res = curl_easy_getinfo(curl, CURLINFO_ACTIVESOCKET, &sockfd);

      res = curl_easy_recv(curl, buf, sizeof(buf), &nread);
      printw("%s", &buf);
    }

    curl_easy_cleanup(curl);
  }
  refresh();
  getch(); // debug

  endwin();
  return EXIT_SUCCESS;
}

WINDOW* create_newwin(int height, int width, int starty, int startx) {
  WINDOW* local_win;
  
  local_win = newwin(height, width, starty, startx);
  box(local_win, 0, 0);
  wrefresh(local_win); // Refresh the window to show the bocx

  return local_win;
}

void destroy_win(WINDOW* local_win) {
  cchar_t del; setcchar(&del, L" ", 0, 0, NULL);
  wborder_set(local_win, &del, &del, &del, &del, &del, &del, &del, &del);
  wrefresh(local_win);
  delwin(local_win);
}

void draw_datestring_W(WINDOW* local_win) {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[20];

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
}

void draw_monthdays_W(WINDOW* local_win) {
  time_t t = time(NULL);
  struct tm* lt = localtime(&t);
  char tstr_buf[7];

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
      printf("%s", &tstr_buf);
    } else { puts("strftime failed"); }
  }
  printf("\n");
}

void draw_weekdays_W(WINDOW* local_win) {
  char* weekdays[7] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

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
}

void draw_hours_W(WINDOW* local_win) {

}